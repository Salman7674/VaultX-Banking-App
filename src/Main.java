import account.BankAccount;
import service.BankService;

public class Main {

    public static void main(String[] args) {

        BankService bank = new BankService();

        // ── 1. Create accounts ────────────────────────────────────────────────
        section("1. CREATING ACCOUNTS");
        BankAccount salman  = bank.createSavingsAccount("Salman Khan",  10000);
        BankAccount priya   = bank.createSavingsAccount("Priya Sharma",  5000);
        BankAccount startup = bank.createCurrentAccount("Startup Corp",  20000);
        bank.printAllAccounts();

        // ── 2. Deposits ───────────────────────────────────────────────────────
        section("2. DEPOSITS");
        bank.deposit(salman.getAccountNumber(),  3000);
        bank.deposit(startup.getAccountNumber(), 15000);

        // Edge case: invalid deposit
        System.out.println("\n  -- Attempting invalid deposit (negative amount) --");
        bank.deposit(salman.getAccountNumber(), -500);

        // ── 3. Withdrawals ────────────────────────────────────────────────────
        section("3. WITHDRAWALS");
        bank.withdraw(salman.getAccountNumber(),  2000);   // valid
        bank.withdraw(startup.getAccountNumber(), 8000);   // valid (current)

        // Edge case: savings min balance violation
        System.out.println("\n  -- Attempting to breach savings minimum balance --");
        bank.withdraw(priya.getAccountNumber(), 4500);     // priya has 5000, min=1000 → max 4000

        // Edge case: current account overdraft usage
        System.out.println("\n  -- Using overdraft on current account --");
        bank.withdraw(startup.getAccountNumber(), 26000);  // valid overdraft

        // Edge case: exceeding overdraft limit
        System.out.println("\n  -- Attempting to exceed overdraft limit --");
        bank.withdraw(startup.getAccountNumber(), 6000);   // would exceed Rs.5000 overdraft

        // ── 4. Transfer ───────────────────────────────────────────────────────
        section("4. TRANSFER");
        bank.deposit(salman.getAccountNumber(), 5000);     // top up first
        bank.transfer(salman.getAccountNumber(), priya.getAccountNumber(), 1500);

        // ── 5. Transaction history ────────────────────────────────────────────
        section("5. TRANSACTION HISTORY");
        bank.printHistory(salman.getAccountNumber());
        bank.printHistory(priya.getAccountNumber());

        // ── 6. Polymorphism demo ──────────────────────────────────────────────
        section("6. POLYMORPHISM DEMO — getAccountType() on parent references");
        BankAccount[] allAccounts = { salman, priya, startup };
        for (BankAccount acc : allAccounts) {
            // Calling overridden method via parent reference — polymorphism in action
            System.out.printf("  %-10s → %s%n",
                    acc.getAccountNumber(), acc.getAccountType());
        }

        // ── 7. Multithreading demo ────────────────────────────────────────────
        section("7. MULTITHREADING DEMO");
        bank.runThreadDemo(
            salman.getAccountNumber(),
            priya.getAccountNumber()
        );

        System.out.println("\n  ✔ All scenarios complete.\n");
    }

    // ── Helper: print a section header ───────────────────────────────────────
    private static void section(String title) {
        System.out.println("\n╔══════════════════════════════════════════════╗");
        System.out.printf( "║  %-44s║%n", title);
        System.out.println("╚══════════════════════════════════════════════╝");
    }
}
