package com.vaultx.banking.controller;

import com.vaultx.banking.account.BankAccount;
import com.vaultx.banking.dto.AmountRequest;
import com.vaultx.banking.dto.CreateAccountRequest;
import com.vaultx.banking.dto.TransferRequest;
import com.vaultx.banking.service.BankService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * REST Controller — exposes all banking operations as HTTP endpoints.
 * The frontend calls these instead of running Java logic in-browser.
 */
@RestController
@RequestMapping("/api")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // ── GET /api/accounts — list all accounts ────────────────────────────────
    @GetMapping("/accounts")
    public List<Map<String, Object>> getAllAccounts() {
        return bankService.getAllAccounts().stream()
                .map(this::accountToMap)
                .toList();
    }

    // ── GET /api/accounts/{accNo} — single account with transactions ─────────
    @GetMapping("/accounts/{accNo}")
    public Map<String, Object> getAccount(@PathVariable String accNo) {
        BankAccount acc = bankService.getAccount(accNo);
        Map<String, Object> map = accountToMap(acc);
        map.put("transactions", acc.getTransactions());
        return map;
    }

    // ── POST /api/accounts — create a new account ────────────────────────────
    @PostMapping("/accounts")
    public Map<String, Object> createAccount(@RequestBody CreateAccountRequest req) {
        BankAccount acc;
        if ("current".equalsIgnoreCase(req.getAccountType())) {
            acc = bankService.createCurrentAccount(req.getHolderName(), req.getInitialDeposit());
        } else {
            acc = bankService.createSavingsAccount(req.getHolderName(), req.getInitialDeposit());
        }
        return accountToMap(acc);
    }

    // ── POST /api/accounts/{accNo}/deposit ───────────────────────────────────
    @PostMapping("/accounts/{accNo}/deposit")
    public Map<String, Object> deposit(@PathVariable String accNo, @RequestBody AmountRequest req) {
        BankAccount acc = bankService.deposit(accNo, req.getAmount());
        return accountToMap(acc);
    }

    // ── POST /api/accounts/{accNo}/withdraw ──────────────────────────────────
    @PostMapping("/accounts/{accNo}/withdraw")
    public Map<String, Object> withdraw(@PathVariable String accNo, @RequestBody AmountRequest req) {
        BankAccount acc = bankService.withdraw(accNo, req.getAmount());
        return accountToMap(acc);
    }

    // ── POST /api/transfer ───────────────────────────────────────────────────
    @PostMapping("/transfer")
    public Map<String, String> transfer(@RequestBody TransferRequest req) {
        bankService.transfer(req.getFromAccount(), req.getToAccount(), req.getAmount());
        return Map.of("message", "Transfer successful");
    }

    // ── GET /api/transactions — all transactions across accounts ─────────────
    @GetMapping("/transactions")
    public List<Map<String, Object>> getAllTransactions(
            @RequestParam(required = false) String account,
            @RequestParam(required = false) String type) {

        List<Map<String, Object>> result = new ArrayList<>();
        for (BankAccount acc : bankService.getAllAccounts()) {
            for (var txn : acc.getTransactions()) {
                // Apply filters
                if (account != null && !account.equals("all")
                        && !acc.getAccountNumber().equals(account)) continue;
                if (type != null && !type.equals("all")
                        && !txn.getType().equals(type)) continue;

                Map<String, Object> map = new LinkedHashMap<>();
                map.put("type", txn.getType());
                map.put("amount", txn.getAmount());
                map.put("note", txn.getNote());
                map.put("timestamp", txn.getTimestamp());
                map.put("accountNumber", acc.getAccountNumber());
                map.put("holderName", acc.getHolderName());
                result.add(map);
            }
        }
        // Sort newest first
        result.sort((a, b) -> ((Comparable) b.get("timestamp")).compareTo(a.get("timestamp")));
        return result;
    }

    // ── GET /api/stats — dashboard statistics ────────────────────────────────
    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        return Map.of(
            "totalBalance", bankService.getTotalBalance(),
            "savingsCount", bankService.getSavingsCount(),
            "currentCount", bankService.getCurrentCount(),
            "transactionCount", bankService.getTotalTransactionCount()
        );
    }

    // ── Helper: convert BankAccount to JSON-friendly map ─────────────────────
    private Map<String, Object> accountToMap(BankAccount acc) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("accountNumber", acc.getAccountNumber());
        map.put("holderName", acc.getHolderName());
        map.put("accountType", acc.getAccountType());
        map.put("balance", acc.getBalance());

        // Include type-specific fields
        if (acc instanceof com.vaultx.banking.account.SavingsAccount sa) {
            map.put("minBalance", sa.getMinBalance());
        } else if (acc instanceof com.vaultx.banking.account.CurrentAccount ca) {
            map.put("overdraftLimit", ca.getOverdraftLimit());
        }
        return map;
    }
}
