/* ═══════════════════════════════════════════════════════════════════
   VaultX Banking Dashboard — Frontend (connected to Spring Boot API)
   All data flows through /api/* endpoints → Java backend
   ═══════════════════════════════════════════════════════════════════ */

const API = '/api';

// ── API Helper ────────────────────────────────────────────────────
async function api(path, options = {}) {
    const res = await fetch(API + path, {
        headers: { 'Content-Type': 'application/json', ...options.headers },
        ...options,
    });
    const data = await res.json();
    if (!res.ok) throw new Error(data.error || 'Request failed');
    return data;
}

// ═══════════════════════════════════════════════════════════════════
// DOM HELPERS
// ═══════════════════════════════════════════════════════════════════
const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

// ── Navigation ────────────────────────────────────────────────────
const navItems = $$('.nav-item');
const views = $$('.view');
const titles = {
    dashboard:    ['Dashboard', 'Overview of your banking activity'],
    accounts:     ['Accounts', 'Create and manage bank accounts'],
    transactions: ['Transactions', 'Deposit, withdraw & view history'],
    transfer:     ['Transfer', 'Move funds between accounts']
};

function switchView(viewName) {
    navItems.forEach(n => n.classList.remove('active'));
    views.forEach(v => v.classList.remove('active'));
    const target = $(`#view-${viewName}`);
    const navTarget = $(`[data-view="${viewName}"]`);
    if (target) target.classList.add('active');
    if (navTarget) navTarget.classList.add('active');
    if (titles[viewName]) {
        $('#page-title').textContent = titles[viewName][0];
        $('#page-subtitle').textContent = titles[viewName][1];
    }
    $('#sidebar').classList.remove('open');
}

navItems.forEach(item => {
    item.addEventListener('click', (e) => {
        e.preventDefault();
        switchView(item.dataset.view);
    });
});

$('#quick-deposit').addEventListener('click', () => { switchView('transactions'); $('#txn-type').value = 'deposit'; });
$('#quick-withdraw').addEventListener('click', () => { switchView('transactions'); $('#txn-type').value = 'withdraw'; });
$('#quick-transfer').addEventListener('click', () => switchView('transfer'));
$('#quick-new-account').addEventListener('click', () => switchView('accounts'));
$('#view-all-txns').addEventListener('click', () => switchView('transactions'));
$('#view-all-accounts').addEventListener('click', () => switchView('accounts'));
$('#mobile-menu-btn').addEventListener('click', () => $('#sidebar').classList.toggle('open'));

// ── Theme Toggle ──────────────────────────────────────────────────
$('#theme-toggle').addEventListener('click', () => {
    document.body.classList.toggle('light-theme');
    localStorage.setItem('theme', document.body.classList.contains('light-theme') ? 'light' : 'dark');
});
if (localStorage.getItem('theme') === 'light') document.body.classList.add('light-theme');

// ── Toast Notifications ───────────────────────────────────────────
function showToast(message, type = 'info') {
    const icons = {
        success: '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><circle cx="9" cy="9" r="8" stroke="currentColor" stroke-width="1.5"/><path d="M5.5 9.5L7.5 11.5L12.5 6.5" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        error: '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><circle cx="9" cy="9" r="8" stroke="currentColor" stroke-width="1.5"/><path d="M6 6L12 12M12 6L6 12" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>',
        info: '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><circle cx="9" cy="9" r="8" stroke="currentColor" stroke-width="1.5"/><path d="M9 8V13M9 5.5V6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>'
    };
    const toast = document.createElement('div');
    toast.className = `toast ${type}`;
    toast.innerHTML = `<span class="toast-icon">${icons[type]}</span><span>${message}</span>`;
    $('#toast-container').appendChild(toast);
    setTimeout(() => { toast.classList.add('removing'); setTimeout(() => toast.remove(), 300); }, 3500);
}

// ── Format Currency ───────────────────────────────────────────────
function formatCurrency(amount) {
    return '₹' + Math.abs(amount).toLocaleString('en-IN', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

// ═══════════════════════════════════════════════════════════════════
// DATA REFRESH (all from real API)
// ═══════════════════════════════════════════════════════════════════

async function refreshAccountSelects() {
    try {
        const accounts = await api('/accounts');
        const selects = ['#txn-account', '#transfer-from', '#transfer-to', '#txn-filter-account'];
        selects.forEach(sel => {
            const el = $(sel);
            const val = el.value;
            const firstOpt = el.querySelector('option');
            el.innerHTML = '';
            el.appendChild(firstOpt);
            accounts.forEach(acc => {
                const opt = document.createElement('option');
                opt.value = acc.accountNumber;
                opt.textContent = `${acc.accountNumber} — ${acc.holderName} (${acc.accountType})`;
                el.appendChild(opt);
            });
            el.value = val;
        });
    } catch (e) { console.error('Failed to load accounts for selects:', e); }
}

async function refreshDashboard() {
    try {
        // Fetch stats from API
        const stats = await api('/stats');
        $('#stat-total-balance').textContent = formatCurrency(stats.totalBalance);
        $('#stat-savings-count').textContent = stats.savingsCount;
        $('#stat-current-count').textContent = stats.currentCount;
        $('#stat-txn-count').textContent = stats.transactionCount;

        // Recent transactions (all, take first 5)
        const txns = await api('/transactions');
        const recentList = $('#recent-txns-list');
        if (txns.length === 0) {
            recentList.innerHTML = `<div class="empty-state"><p>No transactions yet</p><span>Create an account to get started</span></div>`;
        } else {
            recentList.innerHTML = txns.slice(0, 5).map(t => renderTxnItem(t)).join('');
        }

        // Account cards
        const accounts = await api('/accounts');
        const carousel = $('#accounts-carousel');
        if (accounts.length === 0) {
            carousel.innerHTML = `<div class="empty-state"><p>No accounts created</p><span>Click "New Account" to create one</span></div>`;
        } else {
            carousel.innerHTML = accounts.map(acc => {
                const type = acc.accountType.toLowerCase();
                return `
                    <div class="account-card-mini ${type}-card">
                        <div class="mini-card-type">${acc.accountType} Account</div>
                        <div class="mini-card-number">${acc.accountNumber}</div>
                        <div class="mini-card-holder">${acc.holderName}</div>
                        <div class="mini-card-balance">${formatCurrency(acc.balance)}</div>
                    </div>`;
            }).join('');
        }
    } catch (e) { console.error('Dashboard refresh failed:', e); }
}

function renderTxnItem(t) {
    const typeClass = t.type === 'DEPOSIT' ? 'deposit' :
                      t.type === 'WITHDRAW' ? 'withdraw' :
                      t.type === 'TRANSFER-IN' ? 'transfer-in' : 'transfer-out';
    const amtClass = (t.type === 'DEPOSIT' || t.type === 'TRANSFER-IN') ? 'credit' : 'debit';
    const sign = amtClass === 'credit' ? '+' : '-';
    const icons = {
        deposit: '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M9 3V15M5 11L9 15L13 11" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        withdraw: '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M9 15V3M5 7L9 3L13 7" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        'transfer-in': '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M3 9H15M15 9L11 5M15 9L11 13" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>',
        'transfer-out': '<svg width="18" height="18" viewBox="0 0 18 18" fill="none"><path d="M15 9H3M3 9L7 5M3 9L7 13" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/></svg>'
    };
    const time = t.timestamp ? new Date(t.timestamp).toLocaleString('en-IN') : '';
    return `
        <div class="txn-item">
            <div class="txn-icon-box ${typeClass}">${icons[typeClass]}</div>
            <div class="txn-details">
                <div class="txn-note">${t.note}</div>
                <div class="txn-meta">${t.accountNumber || ''} · ${time}</div>
            </div>
            <div class="txn-amount ${amtClass}">${sign}${formatCurrency(t.amount)}</div>
        </div>`;
}

async function refreshAccountsTable() {
    try {
        const accounts = await api('/accounts');
        const wrapper = $('#accounts-table-wrapper');
        $('#total-accounts-badge').textContent = accounts.length;

        if (accounts.length === 0) {
            wrapper.innerHTML = `<div class="empty-state"><p>No accounts yet</p><span>Create your first account above</span></div>`;
            return;
        }
        wrapper.innerHTML = `
            <table class="accounts-table">
                <thead><tr>
                    <th>Account</th><th>Holder</th><th>Type</th><th>Balance</th><th>Actions</th>
                </tr></thead>
                <tbody>
                    ${accounts.map(acc => {
                        const type = acc.accountType.toLowerCase();
                        const info = type === 'savings'
                            ? `Min bal: ₹${(acc.minBalance || 1000).toLocaleString('en-IN')}`
                            : `Overdraft: ₹${(acc.overdraftLimit || 5000).toLocaleString('en-IN')}`;
                        return `<tr>
                            <td><span class="acc-number">${acc.accountNumber}</span></td>
                            <td>${acc.holderName}</td>
                            <td><span class="acc-type-badge ${type}">${acc.accountType}</span><br><small style="color:var(--text-muted);font-size:0.7rem">${info}</small></td>
                            <td><span class="acc-balance">${formatCurrency(acc.balance)}</span></td>
                            <td class="acc-actions">
                                <button class="acc-action-btn" onclick="showAccountHistory('${acc.accountNumber}')">History</button>
                            </td>
                        </tr>`;
                    }).join('')}
                </tbody>
            </table>`;
    } catch (e) { console.error('Accounts table refresh failed:', e); }
}

async function refreshTxnHistory() {
    try {
        const filterAcc = $('#txn-filter-account').value;
        const filterType = $('#txn-filter-type').value;
        let url = `/transactions?account=${filterAcc}&type=${filterType}`;
        const txns = await api(url);

        const list = $('#txn-history-list');
        if (txns.length === 0) {
            list.innerHTML = `<div class="empty-state"><p>No transactions found</p><span>Adjust filters or perform a transaction</span></div>`;
        } else {
            list.innerHTML = txns.map(t => renderTxnItem(t)).join('');
        }
    } catch (e) { console.error('Transaction history refresh failed:', e); }
}

// ── Show Account History Modal (fetches from API) ─────────────────
window.showAccountHistory = async function(accNo) {
    try {
        const acc = await api(`/accounts/${accNo}`);
        const body = $('#modal-body');
        const txns = acc.transactions || [];
        body.innerHTML = `
            <h3 class="modal-title">Transaction History — ${accNo}</h3>
            <p style="color:var(--text-muted);font-size:0.85rem;margin-bottom:16px">${acc.holderName} · ${acc.accountType} · Balance: ${formatCurrency(acc.balance)}</p>
            <div class="modal-txn-list">
                ${txns.length === 0
                    ? '<div class="empty-state"><p>No transactions</p></div>'
                    : txns.slice().reverse().map(t => renderTxnItem({...t, accountNumber: accNo})).join('')
                }
            </div>`;
        $('#modal-overlay').classList.add('active');
    } catch (e) { showToast(e.message, 'error'); }
};

$('#modal-close').addEventListener('click', () => $('#modal-overlay').classList.remove('active'));
$('#modal-overlay').addEventListener('click', (e) => {
    if (e.target === $('#modal-overlay')) $('#modal-overlay').classList.remove('active');
});

// ── Transfer balance display ──────────────────────────────────────
async function refreshTransferBalances() {
    const fromVal = $('#transfer-from').value;
    const toVal = $('#transfer-to').value;
    try {
        $('#transfer-from-balance').textContent = fromVal
            ? `Balance: ${formatCurrency((await api(`/accounts/${fromVal}`)).balance)}`
            : '—';
    } catch { $('#transfer-from-balance').textContent = '—'; }
    try {
        $('#transfer-to-balance').textContent = toVal
            ? `Balance: ${formatCurrency((await api(`/accounts/${toVal}`)).balance)}`
            : '—';
    } catch { $('#transfer-to-balance').textContent = '—'; }
}
$('#transfer-from').addEventListener('change', refreshTransferBalances);
$('#transfer-to').addEventListener('change', refreshTransferBalances);

// ── Master Refresh ────────────────────────────────────────────────
async function refreshAll() {
    await Promise.all([
        refreshDashboard(),
        refreshAccountSelects(),
        refreshAccountsTable(),
        refreshTxnHistory(),
    ]);
}

// ── Filter Listeners ──────────────────────────────────────────────
$('#txn-filter-account').addEventListener('change', refreshTxnHistory);
$('#txn-filter-type').addEventListener('change', refreshTxnHistory);

// ═══════════════════════════════════════════════════════════════════
// FORM HANDLERS (all POST to real API)
// ═══════════════════════════════════════════════════════════════════

// Create Account → POST /api/accounts
$('#create-account-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const name = $('#holder-name').value.trim();
    const type = $('#account-type').value;
    const deposit = parseFloat($('#initial-deposit').value);
    if (!name) { showToast('Please enter a holder name.', 'error'); return; }
    if (isNaN(deposit) || deposit < 0) { showToast('Enter a valid deposit amount.', 'error'); return; }
    try {
        const acc = await api('/accounts', {
            method: 'POST',
            body: JSON.stringify({ holderName: name, accountType: type, initialDeposit: deposit })
        });
        showToast(`${acc.accountType} account ${acc.accountNumber} created for ${name}!`, 'success');
        $('#create-account-form').reset();
        await refreshAll();
    } catch (err) { showToast(err.message, 'error'); }
});

// Deposit / Withdraw → POST /api/accounts/{accNo}/deposit or /withdraw
$('#txn-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const accNo = $('#txn-account').value;
    const type = $('#txn-type').value;
    const amount = parseFloat($('#txn-amount').value);
    if (!accNo) { showToast('Please select an account.', 'error'); return; }
    if (isNaN(amount) || amount <= 0) { showToast('Enter a valid amount.', 'error'); return; }
    try {
        await api(`/accounts/${accNo}/${type}`, {
            method: 'POST',
            body: JSON.stringify({ amount })
        });
        const label = type === 'deposit' ? 'Deposited' : 'Withdrawn';
        showToast(`${label} ${formatCurrency(amount)} ${type === 'deposit' ? 'to' : 'from'} ${accNo}.`, 'success');
        $('#txn-amount').value = '';
        await refreshAll();
    } catch (err) { showToast(err.message, 'error'); }
});

// Transfer → POST /api/transfer
$('#transfer-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const from = $('#transfer-from').value;
    const to = $('#transfer-to').value;
    const amount = parseFloat($('#transfer-amount').value);
    if (!from || !to) { showToast('Select both accounts.', 'error'); return; }
    if (from === to) { showToast('Cannot transfer to the same account.', 'error'); return; }
    if (isNaN(amount) || amount <= 0) { showToast('Enter a valid amount.', 'error'); return; }
    try {
        await api('/transfer', {
            method: 'POST',
            body: JSON.stringify({ fromAccount: from, toAccount: to, amount })
        });
        showToast(`Transferred ${formatCurrency(amount)} from ${from} to ${to}.`, 'success');
        $('#transfer-amount').value = '';
        await refreshAll();
    } catch (err) { showToast(err.message, 'error'); }
});

// ═══════════════════════════════════════════════════════════════════
// INIT — Load data from API on page load
// ═══════════════════════════════════════════════════════════════════
refreshAll();
