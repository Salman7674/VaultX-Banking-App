# 🎯 Salman's Task — Learning Roadmap for VaultX Banking App

> Your personal 5-day study plan. Follow this step by step. Check off each item as you complete it.
> Don't rush. Don't skip. One section at a time.

---

## 📌 Golden Rules Before You Start

1. **Don't try to understand everything at once** — Focus on one file per sitting
2. **Read the code first, then read `project.md`'s explanation** — they go hand-in-hand
3. **Break things on purpose!** Try withdrawing ₹100,000 — see the error. That's learning!
4. **The entire project is just ONE loop repeating:**

```
Browser click → JavaScript fetch() → Controller → Service → Model → JSON back → Page updates
```

Once you understand this loop for ONE feature, you understand ALL features.

---

## 📅 Day 1: Understand the "Why" Before the "How"

### What to Do:
- [ ] Read `project.md` **Section 1** (What Are We Building?)
- [ ] Read `project.md` **Section 2** (The Big Picture — How Web Apps Work)
- [ ] Understand the **Restaurant Analogy** — this is your mental model for the whole project
- [ ] Run the app and play with it:
  ```bash
  cd /Users/salman/Downloads/BankingApp
  ./mvnw spring-boot:run
  # Open http://localhost:8080
  ```
- [ ] Click every button. Create an account. Deposit money. Withdraw. Transfer.
- [ ] Ask yourself: *"When I click Deposit, where does my data go?"*

### Key Takeaway:
> A web app has two halves — **Frontend** (what you see) and **Backend** (what processes your data). They talk to each other through a **REST API** (a set of URLs that accept and return JSON data).

---

## 📅 Day 2: Backend — The Java Side

### What to Read in `project.md`:
- [ ] **Section 3** (Understanding the Backend)
- [ ] **Section 7** (Java OOP Concepts Used)

### Files to Study (in this EXACT order):

#### 1️⃣ `Transaction.java` — Start here (simplest file)
**Path:** `src/main/java/com/vaultx/banking/model/Transaction.java`
- [ ] Read the file — it's just 4 fields: `type`, `amount`, `timestamp`, `note`
- [ ] Understand: This is like an ATM receipt. Every time money moves, one of these is created.
- [ ] **Key concept:** This is a simple POJO (Plain Old Java Object) — just stores data.

#### 2️⃣ `BankAccount.java` — The parent class
**Path:** `src/main/java/com/vaultx/banking/account/BankAccount.java`
- [ ] Notice the word `abstract` — why is it there?
- [ ] Find the `deposit()` method — it's **concrete** (has a body). Both account types share this.
- [ ] Find `withdraw()` — it's **abstract** (no body). Why? Because Savings and Current have DIFFERENT rules!
- [ ] **Key concept:** Abstraction — define WHAT to do, not HOW to do it.

#### 3️⃣ `SavingsAccount.java` — The safe choice
**Path:** `src/main/java/com/vaultx/banking/account/SavingsAccount.java`
- [ ] Find `extends BankAccount` — this means it INHERITS everything from BankAccount
- [ ] Read the `withdraw()` method — what rule does it enforce? (Answer: minimum balance ₹1,000)
- [ ] Notice `@Override` — this means "I'm providing MY version of the parent's method"
- [ ] **Key concept:** Inheritance + Polymorphism

#### 4️⃣ `CurrentAccount.java` — The flexible choice
**Path:** `src/main/java/com/vaultx/banking/account/CurrentAccount.java`
- [ ] Compare its `withdraw()` with SavingsAccount's `withdraw()` — SAME method name, DIFFERENT logic
- [ ] This one allows overdraft (balance can go negative, up to -₹5,000)
- [ ] **Key concept:** This IS polymorphism — same method, different behavior based on the object type

#### 5️⃣ `BankService.java` — The brain
**Path:** `src/main/java/com/vaultx/banking/service/BankService.java`
- [ ] Find `Map<String, BankAccount> accounts` — this is like a phone book (account number → account)
- [ ] Read `createSavingsAccount()` — how does it generate account numbers? (SAV1001, SAV1002...)
- [ ] Read `transfer()` — why is it `synchronized`? (Prevents two transfers from clashing)
- [ ] Notice `@Service` — this tells Spring Boot to manage this class
- [ ] **Key concept:** Service layer pattern — separates business logic from web handling

### Day 2 Self-Test:
Ask yourself these questions:
- [ ] Why can't we do `new BankAccount()`?
- [ ] What happens if I try to withdraw ₹4,500 from a Savings account with ₹5,000?
- [ ] What's the difference between Savings and Current withdrawal rules?
- [ ] Where are all accounts stored? (Answer: HashMap inside BankService)

---

## 📅 Day 3: The Bridge — Spring Boot (Controller + DTOs)

### What to Read in `project.md`:
- [ ] **Section 5** (The Bridge — How Frontend Talks to Backend)
- [ ] **Section 8** (Spring Boot Concepts)

### Files to Study:

#### 1️⃣ `BankingApplication.java` — Where it all starts
**Path:** `src/main/java/com/vaultx/banking/BankingApplication.java`
- [ ] Find `@SpringBootApplication` — this ONE annotation does three things:
  - Scans for all `@Controller`, `@Service` classes
  - Configures everything automatically
  - Starts the embedded Tomcat server
- [ ] Find `CommandLineRunner seedData()` — this runs on startup and creates demo accounts
- [ ] **Key concept:** Spring Boot auto-configures everything for you

#### 2️⃣ DTOs — The envelopes
**Path:** `src/main/java/com/vaultx/banking/dto/`
- [ ] Read `CreateAccountRequest.java` — just 3 fields + getters/setters
- [ ] Read `AmountRequest.java` — just 1 field (amount)
- [ ] Read `TransferRequest.java` — 3 fields (from, to, amount)
- [ ] **Why do these exist?** They represent what the FRONTEND sends. Spring Boot converts JSON → these objects automatically.

#### 3️⃣ `BankController.java` — ⭐ THE MOST IMPORTANT FILE ⭐
**Path:** `src/main/java/com/vaultx/banking/controller/BankController.java`
- [ ] Find `@RestController` — means "I return JSON, not HTML pages"
- [ ] Find `@RequestMapping("/api")` — means "all my URLs start with /api"
- [ ] For EACH method, identify:
  - [ ] The annotation (`@GetMapping` or `@PostMapping`) — what URL does it handle?
  - [ ] The parameters (`@PathVariable`, `@RequestBody`) — where does data come from?
  - [ ] What service method it calls
  - [ ] What it returns

**Study these mappings:**

| URL | Annotation | What Happens |
|-----|-----------|-------------|
| `GET /api/accounts` | `@GetMapping("/accounts")` | Returns list of all accounts |
| `POST /api/accounts` | `@PostMapping("/accounts")` | Creates a new account |
| `POST /api/accounts/SAV1001/deposit` | `@PostMapping("/accounts/{accNo}/deposit")` | Deposits money |
| `POST /api/accounts/SAV1001/withdraw` | `@PostMapping("/accounts/{accNo}/withdraw")` | Withdraws money |
| `POST /api/transfer` | `@PostMapping("/transfer")` | Transfers between accounts |
| `GET /api/stats` | `@GetMapping("/stats")` | Returns dashboard numbers |

#### 4️⃣ `GlobalExceptionHandler.java` — The safety net
**Path:** `src/main/java/com/vaultx/banking/exception/GlobalExceptionHandler.java`
- [ ] Read how it catches `InsufficientFundsException` and returns a clean JSON error
- [ ] Without this, errors would show ugly stack traces to the user
- [ ] **Key concept:** `@RestControllerAdvice` = a global error catcher

### Day 3 Hands-On Exercise:
Open a terminal and test the API directly with `curl`:
```bash
# Get all accounts
curl http://localhost:8080/api/accounts

# Get dashboard stats
curl http://localhost:8080/api/stats

# Create a new account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"holderName":"Test User","accountType":"savings","initialDeposit":5000}'

# Deposit money
curl -X POST http://localhost:8080/api/accounts/SAV1001/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount":2000}'

# Try an invalid withdrawal (should fail!)
curl -X POST http://localhost:8080/api/accounts/SAV1001/withdraw \
  -H "Content-Type: application/json" \
  -d '{"amount":999999}'
```
- [ ] Did you run all 5 commands?
- [ ] Did the last one return an error message? That's `GlobalExceptionHandler` in action!

---

## 📅 Day 4: Frontend — The Browser Side

### What to Read in `project.md`:
- [ ] **Section 4** (Understanding the Frontend)
- [ ] **Section 9** (Frontend Concepts)

### Files to Study:

#### 1️⃣ `index.html` — The skeleton
**Path:** `src/main/resources/static/index.html`
- [ ] Don't read every line. Instead, search for these key elements:
  - [ ] Find the **sidebar** (`<aside class="sidebar">`) — the navigation
  - [ ] Find the **4 views** (`id="view-dashboard"`, `view-accounts`, `view-transactions`, `view-transfer`)
  - [ ] Find the **forms** — these are where you type data
  - [ ] Find the **toast container** (`id="toast-container"`) — where success/error popups appear
- [ ] **Key concept:** HTML defines STRUCTURE. It's like the blueprint of a house.

#### 2️⃣ `styles.css` — The clothes
**Path:** `src/main/resources/static/styles.css`
- [ ] Don't memorize CSS. Just notice these cool techniques:
  - [ ] **CSS Variables** (`:root { --accent: #6366f1; }`) — define colors once, use everywhere
  - [ ] **Glassmorphism** (`backdrop-filter: blur(16px)`) — the frosted glass effect
  - [ ] **Animations** (`@keyframes fadeSlide`) — smooth transitions
  - [ ] **Responsive design** (`@media (max-width: 768px)`) — adapts to mobile
- [ ] **Key concept:** CSS makes things beautiful. The HTML stays the same.

#### 3️⃣ `app.js` — ⭐ THE BRIDGE FILE ⭐
**Path:** `src/main/resources/static/app.js`

This is where the **frontend meets the backend**. Do this exercise:

- [ ] Find the `api()` helper function at the top — this is a wrapper around `fetch()`
- [ ] Search for every `await api(` call in the file. Each one talks to Java!
- [ ] For each one, answer: "What URL does it call? What data does it send? What does it do with the response?"

**Key `fetch()` calls to understand:**

| What User Does | JavaScript Code | Backend URL |
|----------------|----------------|-------------|
| Page loads | `await api('/stats')` | `GET /api/stats` |
| Page loads | `await api('/accounts')` | `GET /api/accounts` |
| Creates account | `await api('/accounts', {method:'POST', body:...})` | `POST /api/accounts` |
| Deposits money | `await api('/accounts/SAV1001/deposit', {method:'POST', body:...})` | `POST /api/accounts/SAV1001/deposit` |
| Transfers money | `await api('/transfer', {method:'POST', body:...})` | `POST /api/transfer` |

- [ ] **Key concept:** `fetch()` is JavaScript's way of making HTTP requests — it's the bridge between browser and server.

---

## 📅 Day 5: Connect the Dots — Trace a Full Request

### The Big Exercise: Trace "Create Account" End-to-End

Do this with the actual files open. Follow the data like a detective:

#### Step 1: The Button Click
- [ ] Open `index.html` → find the form with `id="create-account-form"`
- [ ] Notice the inputs: `holder-name`, `account-type`, `initial-deposit`

#### Step 2: JavaScript Catches the Submit
- [ ] Open `app.js` → find `$('#create-account-form').addEventListener('submit', ...)`
- [ ] See how it reads the form values with `$('#holder-name').value`
- [ ] See the `fetch()` call:
  ```javascript
  await api('/accounts', {
      method: 'POST',
      body: JSON.stringify({ holderName: name, accountType: type, initialDeposit: deposit })
  });
  ```
- [ ] This sends JSON to the server: `{"holderName":"Danish","accountType":"savings","initialDeposit":8000}`

#### Step 3: Spring Boot Receives It
- [ ] Open `BankController.java` → find `@PostMapping("/accounts")`
- [ ] See `@RequestBody CreateAccountRequest req` — Spring converts the JSON into a Java object
- [ ] See it calls `bankService.createSavingsAccount(req.getHolderName(), req.getInitialDeposit())`

#### Step 4: BankService Creates the Account
- [ ] Open `BankService.java` → find `createSavingsAccount()`
- [ ] See it generates account number: `"SAV" + (++accountCounter)` → "SAV1004"
- [ ] Creates: `new SavingsAccount("SAV1004", "Danish", 8000)`
- [ ] Stores it in the HashMap: `accounts.put("SAV1004", account)`

#### Step 5: Response Goes Back
- [ ] Back in `BankController.java` → see `return accountToMap(acc)`
- [ ] This converts the Java object to a Map, which Spring turns into JSON:
  ```json
  {"accountNumber":"SAV1004","holderName":"Danish","balance":8000.0,"accountType":"Savings"}
  ```

#### Step 6: JavaScript Updates the Page
- [ ] Back in `app.js` → after the `await api(...)` call
- [ ] See `showToast(...)` — shows the green success message
- [ ] See `await refreshAll()` — re-fetches all data and redraws the page

### ✅ You Just Traced the ENTIRE Full-Stack Flow!

- [ ] Did you follow all 6 steps?
- [ ] Can you do the same trace for **Deposit**? (Hint: same pattern, different URL)
- [ ] Can you do it for **Transfer**? (Hint: involves TWO accounts)

---

## 🏆 Completion Checklist

After 5 days, you should be able to answer YES to all of these:

- [ ] I can explain what a REST API is in my own words
- [ ] I know the difference between GET and POST
- [ ] I understand what `@RestController`, `@GetMapping`, `@PostMapping` do
- [ ] I can trace a button click from HTML → JavaScript → Controller → Service → Model → JSON → back to browser
- [ ] I understand why we use `abstract class` for BankAccount
- [ ] I can explain polymorphism using SavingsAccount vs CurrentAccount withdraw()
- [ ] I know what `fetch()` does in JavaScript
- [ ] I understand what `@RequestBody` and `@PathVariable` do
- [ ] I know why `BankService` is annotated with `@Service`
- [ ] I can create a new account using `curl` from the terminal

---

## 📝 Quick Reference — Files to Remember

| File | Role | One-Line Summary |
|------|------|-----------------|
| `BankingApplication.java` | Entry Point | Starts the app, seeds demo data |
| `BankController.java` | Waiter | Receives HTTP requests, returns JSON |
| `BankService.java` | Chef | Contains all business logic |
| `BankAccount.java` | Blueprint | Abstract parent for all accounts |
| `SavingsAccount.java` | Account Type | Enforces min balance ₹1,000 |
| `CurrentAccount.java` | Account Type | Allows overdraft up to ₹5,000 |
| `Transaction.java` | Receipt | Records every money movement |
| `DTOs` | Envelopes | Carry data from frontend to backend |
| `GlobalExceptionHandler.java` | Safety Net | Catches errors, returns clean messages |
| `app.js` | Bridge | JavaScript that calls Java via fetch() |
| `index.html` | Structure | The page layout (HTML) |
| `styles.css` | Beauty | Colors, animations, responsive design |

---

> **You've got this, Salman! 💪** Take it one day at a time. By Day 5, you'll understand how every real-world web application works — because they ALL follow the same pattern.
