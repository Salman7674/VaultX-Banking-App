# 📖 VaultX Banking App — Complete Project Documentation

> **This document is your one-stop guide.** Read it top to bottom, and you'll understand every single concept used in this project — from what Java is to how the frontend talks to the backend. No prior web development experience needed.

---

## Table of Contents

1. [What Are We Building?](#1-what-are-we-building)
2. [The Big Picture — How Web Apps Work](#2-the-big-picture--how-web-apps-work)
3. [Understanding the Backend (Java + Spring Boot)](#3-understanding-the-backend-java--spring-boot)
4. [Understanding the Frontend (HTML + CSS + JS)](#4-understanding-the-frontend-html--css--js)
5. [The Bridge — How Frontend Talks to Backend (REST API)](#5-the-bridge--how-frontend-talks-to-backend-rest-api)
6. [File-by-File Deep Dive](#6-file-by-file-deep-dive)
7. [Java OOP Concepts Used](#7-java-oop-concepts-used)
8. [Spring Boot Concepts](#8-spring-boot-concepts)
9. [Frontend Concepts](#9-frontend-concepts)
10. [Common Errors & How to Fix Them](#10-common-errors--how-to-fix-them)
11. [Glossary](#11-glossary)

---

## 1. What Are We Building?

We're building a **banking dashboard** — a web application where you can:

- ✅ Create bank accounts (Savings & Current)
- ✅ Deposit money into accounts
- ✅ Withdraw money from accounts
- ✅ Transfer money between accounts
- ✅ View transaction history

This is similar to what you see when you log into your bank's website — except we built it from scratch!

### Why This Project?

This project teaches you the **complete journey** of a web request:

```
You click a button → JavaScript sends data → Java processes it → Result comes back → Page updates
```

Every real-world app (Amazon, Flipkart, Zomato, your bank's website) works exactly like this.

---

## 2. The Big Picture — How Web Apps Work

### The Two Halves of Every Web App

Every website you've ever used has two parts:

```
┌─────────────────────────────────────────────────────────────┐
│                        YOUR BROWSER                         │
│                                                             │
│   ┌───────────────────────────────────────────────────┐     │
│   │              FRONTEND                             │     │
│   │   • What you SEE (buttons, text, colors)          │     │
│   │   • HTML = structure (skeleton)                   │     │
│   │   • CSS  = design (clothes & makeup)              │     │
│   │   • JS   = behavior (brain & muscles)             │     │
│   └───────────────────────────────────────────────────┘     │
│                          │                                   │
│                    HTTP Request                               │
│                    (like sending                              │
│                     a letter)                                │
│                          │                                   │
└──────────────────────────│──────────────────────────────────┘
                           │
                     THE INTERNET
                           │
┌──────────────────────────│──────────────────────────────────┐
│                          ▼                                   │
│   ┌───────────────────────────────────────────────────┐     │
│   │              BACKEND                              │     │
│   │   • What you DON'T see                            │     │
│   │   • Java code running on a server                 │     │
│   │   • Processes your requests                       │     │
│   │   • Stores and retrieves data                     │     │
│   │   • Enforces rules (can't withdraw more           │     │
│   │     than your balance, etc.)                      │     │
│   └───────────────────────────────────────────────────┘     │
│                        SERVER                                │
└─────────────────────────────────────────────────────────────┘
```

### Real-World Analogy: The Restaurant 🍽️

| Restaurant | Web App |
|---|---|
| **You** (the customer) | The user sitting in front of their browser |
| **The menu** | The frontend (HTML page with buttons & forms) |
| **The waiter** | The REST API Controller (`BankController.java`) |
| **Your order** | The HTTP request (e.g., "deposit ₹5000") |
| **The chef** | The Service layer (`BankService.java`) |
| **Ingredients & recipes** | The model classes (`BankAccount.java`, etc.) |
| **Your food arrives** | The JSON response (data sent back to browser) |

**You never go into the kitchen.** The waiter takes your order and brings back food. Similarly, **JavaScript never directly runs Java code.** It sends a request and gets back data.

---

## 3. Understanding the Backend (Java + Spring Boot)

### What is a Backend?

The backend is the **brain** of the application. It:
- Receives requests from the frontend ("Hey, deposit ₹5000 into SAV1001")
- Processes them using Java code (checks rules, updates balances)
- Sends back results ("Done! New balance is ₹15,000")

### What is Spring Boot?

Imagine you want to build a house. You could:
- **Option A:** Make every brick yourself, cut every piece of wood, forge every nail — painful!
- **Option B:** Buy pre-made walls, doors, and roofing kits, then assemble them — much faster!

**Spring Boot is Option B for Java web applications.** It gives you:
- A built-in web server (Tomcat) — so you don't need to install one
- Automatic JSON conversion — Java objects become JSON and vice versa
- Easy URL routing — map URLs to Java methods with simple annotations
- Dependency injection — Spring creates and connects your classes for you

### What is Maven?

Maven is like a **shopping list manager** for your project. Your project needs certain libraries (Spring Boot, Tomcat, Jackson, etc.). Instead of downloading each one manually, you write what you need in `pom.xml`, and Maven fetches everything:

```xml
<!-- pom.xml says: "I need Spring Boot Web" -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Maven reads this and downloads Spring Boot + Tomcat + Jackson + 50 other things automatically.

### What is Tomcat?

When you run the app, a web server called **Tomcat** starts inside it. Tomcat is like a **receptionist** — it sits on port 8080 and listens for incoming requests. When your browser sends a request to `http://localhost:8080/api/accounts`, Tomcat receives it and passes it to Spring Boot to handle.

### What is JSON?

JSON (JavaScript Object Notation) is a **universal language** for data. Both Java and JavaScript can read it. It looks like this:

```json
{
    "accountNumber": "SAV1001",
    "holderName": "Salman Khan",
    "balance": 14500.00,
    "accountType": "Savings"
}
```

Think of JSON as a **common language** — like English being spoken between a French person and a Japanese person. Java "speaks" Java, JavaScript "speaks" JavaScript, but they both understand JSON.

---

## 4. Understanding the Frontend (HTML + CSS + JS)

### What is the Frontend?

The frontend is everything that runs **in your browser**. It's what you see and interact with.

### The Three Languages of the Web

| Language | What it does | Analogy |
|----------|-------------|---------|
| **HTML** | Creates the structure — buttons, inputs, text | The **skeleton** of a body |
| **CSS** | Makes it look beautiful — colors, spacing, animations | The **clothes & makeup** |
| **JavaScript** | Makes it interactive — click handling, API calls | The **brain & muscles** |

### HTML Example from Our App

```html
<!-- This creates a "Deposit" button -->
<button class="action-btn action-deposit" id="quick-deposit">
    <span>Deposit</span>
</button>
```

- `class` = which CSS style to apply (like picking an outfit)
- `id` = unique name so JavaScript can find this button

### CSS Example from Our App

```css
/* This makes buttons glow when you hover over them */
.action-btn:hover {
    border-color: rgba(99, 102, 241, 0.35);
    transform: translateY(-2px);  /* Float up slightly */
}
```

### JavaScript Example from Our App

```javascript
// When "Deposit" button is clicked, send data to Java backend
$('#quick-deposit').addEventListener('click', async () => {
    const response = await fetch('/api/accounts/SAV1001/deposit', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount: 5000 })
    });
    const result = await response.json();
    // result now contains the updated account from Java!
});
```

---

## 5. The Bridge — How Frontend Talks to Backend (REST API)

This is the **heart** of full-stack development. Let's break it down completely.

### What is an API?

API stands for **Application Programming Interface**. It's a set of **rules** for how two programs can talk to each other.

Think of it like a **power socket**:
- The socket has a standard shape (the API)
- Any plug that matches can use it
- You don't need to know how electricity works inside the wall

Our API is a set of URLs that accept and return JSON data.

### What is REST?

REST is a **style** of building APIs. It uses standard HTTP methods:

| HTTP Method | Meaning | Example |
|-------------|---------|---------|
| `GET` | "Give me data" | Get all accounts |
| `POST` | "Here's new data, process it" | Create account, deposit money |
| `PUT` | "Update this existing data" | (Not used in our app) |
| `DELETE` | "Remove this data" | (Not used in our app) |

### Complete Journey of a Deposit Request

Let's trace what happens when you deposit ₹5,000 into account SAV1001:

#### Step 1: You Click the Button
You select SAV1001, type 5000, and click "Execute" on the Transactions page.

#### Step 2: JavaScript Sends an HTTP Request
```javascript
// app.js — line runs when you click "Execute"
await fetch('/api/accounts/SAV1001/deposit', {
    method: 'POST',                              // I'm SENDING data
    headers: { 'Content-Type': 'application/json' },  // It's in JSON format
    body: JSON.stringify({ amount: 5000 })        // Here's the data
});
```

This sends the following HTTP request through the network:

```
POST /api/accounts/SAV1001/deposit HTTP/1.1
Host: localhost:8080
Content-Type: application/json

{"amount": 5000}
```

#### Step 3: Spring Boot Receives the Request

Tomcat (the web server) receives this and looks at the URL `/api/accounts/SAV1001/deposit`. Spring Boot checks: "Which Java method handles this URL?"

It finds this in `BankController.java`:

```java
@PostMapping("/accounts/{accNo}/deposit")
public Map<String, Object> deposit(@PathVariable String accNo,
                                    @RequestBody AmountRequest req) {
    // accNo = "SAV1001" (from the URL)
    // req.getAmount() = 5000.0 (from the JSON body)
    BankAccount acc = bankService.deposit(accNo, req.getAmount());
    return accountToMap(acc);
}
```

**What happened here:**
- `@PostMapping` = "I handle POST requests to this URL"
- `@PathVariable` = "Take `SAV1001` from the URL and put it in `accNo`"
- `@RequestBody` = "Take the JSON `{"amount": 5000}` and convert it into a Java object"

#### Step 4: BankService Processes the Logic

```java
// BankService.java
public BankAccount deposit(String accountNumber, double amount) {
    BankAccount account = getAccount(accountNumber);  // Find account SAV1001
    account.deposit(amount);                          // Add ₹5000 to balance
    return account;                                   // Return updated account
}
```

#### Step 5: BankAccount Updates the Balance

```java
// BankAccount.java
public void deposit(double amount) {
    if (amount <= 0) {
        throw new InvalidAmountException("Amount must be positive!");
    }
    balance += amount;  // 14500 + 5000 = 19500
    transactions.add(new Transaction("DEPOSIT", amount, "Deposit"));
}
```

#### Step 6: Java Object → JSON Response

Spring Boot automatically converts the Java object into JSON:

```json
{
    "accountNumber": "SAV1001",
    "holderName": "Salman Khan",
    "balance": 19500.00,
    "accountType": "Savings",
    "minBalance": 1000.0
}
```

#### Step 7: JavaScript Updates the Page

```javascript
// app.js — after the response comes back
const result = await response.json();  // Parse the JSON
showToast('Deposited ₹5,000 to SAV1001!', 'success');
await refreshAll();  // Re-fetch all data and update the screen
```

The page now shows the new balance of ₹19,500. The entire round trip took milliseconds!

### Why Can't JavaScript Just Run Java Directly?

Great question! Because:
1. **They run in different places** — JavaScript runs in your browser, Java runs on the server
2. **Security** — You don't want users running arbitrary code on your server
3. **Separation** — Frontend and backend can be built by different teams
4. **Flexibility** — You could replace the frontend with a mobile app and the backend stays the same

---

## 6. File-by-File Deep Dive

### 📄 `pom.xml` — The Shopping List

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.5</version>
</parent>
```

This tells Maven: "My project is based on Spring Boot 3.4.5. Download everything I need."

---

### 📄 `BankingApplication.java` — The Starting Point

```java
@SpringBootApplication      // ← "This is a Spring Boot app"
public class BankingApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingApplication.class, args);
        // This one line does SO much:
        // 1. Starts Tomcat web server on port 8080
        // 2. Scans for @Controller, @Service classes
        // 3. Creates instances of them and connects them
        // 4. Starts listening for HTTP requests
    }
}
```

The `@Bean CommandLineRunner seedData()` method runs automatically after startup and creates 3 demo accounts with some transactions — so you have data to see immediately.

---

### 📄 `BankAccount.java` — The Abstract Parent

```java
public abstract class BankAccount {
    protected String accountNumber;    // "SAV1001"
    protected String holderName;       // "Salman Khan"
    protected double balance;          // 14500.00
    protected List<Transaction> transactions;  // History of all operations
}
```

**Why `abstract`?** Because you can't have a generic "bank account" — it's either a Savings or Current account. The `abstract` keyword says: "You can't create a BankAccount directly. You must create a SavingsAccount or CurrentAccount."

---

### 📄 `SavingsAccount.java` — The Safe Choice

```java
public class SavingsAccount extends BankAccount {
    private double minBalance = 1000.0;  // Must keep at least ₹1000

    @Override
    public void withdraw(double amount) {
        if (balance - amount < minBalance) {
            throw new InsufficientFundsException("Can't go below ₹1000!");
        }
        balance -= amount;
    }
}
```

**Key rule:** Can't withdraw if it would leave less than ₹1,000 in the account.

---

### 📄 `CurrentAccount.java` — The Flexible Choice

```java
public class CurrentAccount extends BankAccount {
    private double overdraftLimit = 5000.0;  // Can go ₹5000 into negative

    @Override
    public void withdraw(double amount) {
        if (balance - amount < -overdraftLimit) {
            throw new InsufficientFundsException("Overdraft limit exceeded!");
        }
        balance -= amount;
    }
}
```

**Key rule:** Can go negative, but not more than ₹5,000 below zero.

---

### 📄 `Transaction.java` — The Receipt

```java
public class Transaction {
    private String type;              // "DEPOSIT", "WITHDRAW", "TRANSFER-IN", "TRANSFER-OUT"
    private double amount;            // 5000.00
    private LocalDateTime timestamp;  // When it happened
    private String note;              // "Deposit" or "Transfer from SAV1002"
}
```

Every time money moves, a Transaction is created — like a receipt from an ATM.

---

### 📄 `BankService.java` — The Brain

This is the most important class. It:
- Stores all accounts in a `HashMap` (like a phone book — look up by account number)
- Creates new accounts with auto-generated numbers (SAV1001, SAV1002, CUR1003...)
- Processes deposits, withdrawals, and transfers
- Uses `synchronized` on transfers to prevent problems when two requests happen at the same time

```java
@Service  // ← Tells Spring: "Create one instance of this and share it everywhere"
public class BankService {
    private Map<String, BankAccount> accounts = new HashMap<>();
    // Key = "SAV1001", Value = SavingsAccount object
}
```

---

### 📄 `BankController.java` — The Waiter

This is the **bridge** between frontend and backend. Each method handles a specific URL:

```java
@RestController           // ← "I return JSON, not HTML"
@RequestMapping("/api")   // ← "All my URLs start with /api"
public class BankController {

    @GetMapping("/accounts")          // GET /api/accounts
    public List<Map> getAllAccounts() { ... }

    @PostMapping("/accounts")         // POST /api/accounts
    public Map createAccount(@RequestBody CreateAccountRequest req) { ... }

    @PostMapping("/accounts/{accNo}/deposit")  // POST /api/accounts/SAV1001/deposit
    public Map deposit(@PathVariable String accNo, @RequestBody AmountRequest req) { ... }
}
```

**Annotations are like labels** that tell Spring what each method does:
- `@GetMapping` = handles GET requests (fetching data)
- `@PostMapping` = handles POST requests (sending data)
- `@PathVariable` = extracts values from the URL
- `@RequestBody` = parses the JSON body into a Java object

---

### 📄 DTOs — The Envelopes

DTO = **Data Transfer Object**. They're simple classes that represent what the frontend sends:

```java
// When frontend sends: {"holderName": "Danish", "accountType": "savings", "initialDeposit": 8000}
public class CreateAccountRequest {
    private String holderName;
    private String accountType;
    private double initialDeposit;
    // + getters and setters
}
```

**Why use DTOs?** Because the frontend sends different data than what our model classes look like. DTOs act as a "translation layer."

---

### 📄 `GlobalExceptionHandler.java` — The Safety Net

Without this, if something goes wrong (e.g., withdrawing too much), Spring Boot would return an ugly 500 error with a Java stack trace. The exception handler catches errors and returns clean JSON:

```java
@RestControllerAdvice  // ← "I handle errors for all controllers"
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<Map> handleInsufficientFunds(InsufficientFundsException e) {
        // Instead of a crash, return:  {"error": "Can't withdraw ₹50,000..."}
        return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
    }
}
```

---

### 📄 `app.js` — The Frontend Brain

This file was **completely rewritten** to talk to the backend. Before, it had Java-like classes running in the browser. Now, it uses `fetch()`:

```javascript
// BEFORE (client-side logic — fake):
class BankAccount {
    deposit(amount) { this.balance += amount; }
}

// AFTER (talks to real Java backend):
async function deposit(accNo, amount) {
    const response = await fetch(`/api/accounts/${accNo}/deposit`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ amount })
    });
    return await response.json();  // Real data from Java!
}
```

---

## 7. Java OOP Concepts Used

### 1. Abstraction (`abstract class BankAccount`)

**What:** Hiding complex details and showing only what's necessary.

**In our app:** `BankAccount` says "every account can deposit and withdraw" but doesn't define HOW to withdraw (because savings and current have different rules). Each subclass fills in the details.

```java
public abstract class BankAccount {
    public abstract void withdraw(double amount);  // WHAT to do, not HOW
}
```

### 2. Inheritance (`SavingsAccount extends BankAccount`)

**What:** A class getting properties and methods from a parent class.

**In our app:** Both SavingsAccount and CurrentAccount inherit `accountNumber`, `balance`, `deposit()` from BankAccount. They don't repeat the code — they just add their own rules.

```java
public class SavingsAccount extends BankAccount {
    // Gets accountNumber, balance, deposit() for free!
    // Only needs to define its own withdraw() logic
}
```

### 3. Polymorphism (one method, different behaviors)

**What:** The same method name behaving differently depending on the object type.

**In our app:** Calling `withdraw()` on a SavingsAccount checks minimum balance. Calling `withdraw()` on a CurrentAccount checks overdraft. Same method name, different behavior.

```java
BankAccount account1 = new SavingsAccount(...);
BankAccount account2 = new CurrentAccount(...);
account1.withdraw(5000);  // Checks min balance
account2.withdraw(5000);  // Checks overdraft
```

### 4. Encapsulation (protecting data)

**What:** Keeping internal data private and only accessing it through methods.

**In our app:** `balance` is `protected` — you can't do `account.balance = 1000000` from outside. You MUST call `deposit()` or `withdraw()`, which enforce the rules.

### 5. Exception Handling (try-catch)

**What:** Gracefully handling errors instead of crashing.

**In our app:** If you try to withdraw ₹50,000 from an account with ₹5,000, it doesn't crash. It throws an `InsufficientFundsException`, which gets caught and sent back as a clean error message.

---

## 8. Spring Boot Concepts

### 1. `@SpringBootApplication`
Tells Java: "This is the main class. Start everything from here." It combines three annotations: component scanning, auto-configuration, and configuration.

### 2. `@RestController`
Tells Spring: "This class handles web requests and returns JSON data (not HTML pages)."

### 3. `@Service`
Tells Spring: "Create one instance of this class and reuse it everywhere." This is called a **Singleton** — there's only one BankService, shared by all requests.

### 4. `@GetMapping` / `@PostMapping`
Maps a URL to a Java method:
```java
@GetMapping("/accounts")       // When someone visits /api/accounts...
public List getAllAccounts() {  // ...run this method and return the result as JSON
}
```

### 5. `@RequestBody`
Tells Spring: "Take the JSON from the request body and convert it into this Java object."

```java
// Frontend sends: {"amount": 5000}
// Spring converts it to: AmountRequest object where getAmount() returns 5000.0
```

### 6. `@PathVariable`
Extracts a value from the URL:
```java
@GetMapping("/accounts/{accNo}")
public Map getAccount(@PathVariable String accNo) {
    // If URL is /api/accounts/SAV1001, then accNo = "SAV1001"
}
```

### 7. Dependency Injection
Instead of YOU creating objects, Spring creates them and gives them to you:

```java
// You DON'T write: BankService service = new BankService();
// Instead, Spring creates it and "injects" it:

public class BankController {
    private final BankService bankService;

    public BankController(BankService bankService) {
        // Spring automatically passes the BankService instance here!
        this.bankService = bankService;
    }
}
```

### 8. `CommandLineRunner`
A method that runs automatically when the app starts. We use it to seed demo data:
```java
@Bean
CommandLineRunner seedData(BankService bank) {
    return args -> {
        bank.createSavingsAccount("Salman Khan", 10000);  // Auto-runs on startup!
    };
}
```

---

## 9. Frontend Concepts

### 1. `fetch()` — Making HTTP Requests from JavaScript

`fetch()` is the built-in browser function for sending HTTP requests:

```javascript
// GET request (fetch data)
const response = await fetch('/api/accounts');
const accounts = await response.json();

// POST request (send data)
const response = await fetch('/api/accounts', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ holderName: 'Danish', accountType: 'savings', initialDeposit: 8000 })
});
```

### 2. `async/await` — Waiting for Responses

Network requests take time. `async/await` lets you wait for them cleanly:

```javascript
// Without async/await (messy):
fetch('/api/accounts').then(res => res.json()).then(data => console.log(data));

// With async/await (clean):
const response = await fetch('/api/accounts');
const data = await response.json();
console.log(data);
```

### 3. DOM Manipulation — Updating the Page

After getting data from the API, JavaScript updates what you see:

```javascript
// Change the text of an element
document.querySelector('#stat-total-balance').textContent = '₹48,000.00';

// Add new HTML inside an element
document.querySelector('#accounts-table').innerHTML = '<tr><td>SAV1001</td></tr>';
```

### 4. Event Listeners — Reacting to Clicks

```javascript
// When the "Create Account" form is submitted:
document.querySelector('#create-account-form').addEventListener('submit', async (e) => {
    e.preventDefault();  // Don't reload the page!
    // ... send data to API ...
});
```

### 5. CSS Glassmorphism — The Frosted Glass Effect

```css
.card {
    background: rgba(17, 24, 39, 0.7);      /* Semi-transparent */
    backdrop-filter: blur(16px);              /* Blur what's behind */
    border: 1px solid rgba(99, 102, 241, 0.15); /* Subtle border */
}
```

This creates the modern "frosted glass" look you see in the cards.

---

## 10. Common Errors & How to Fix Them

### "Port 8080 is already in use"
Another app is using port 8080. Kill it:
```bash
lsof -i :8080          # Find what's using it
kill -9 <PID>           # Kill it
./mvnw spring-boot:run  # Try again
```

### "Connection refused" in browser
The Spring Boot app isn't running. Start it:
```bash
./mvnw spring-boot:run
```

### "Account not found: SAV1001"
The app stores data in memory. When you restart the app, all data resets to the demo data. This is normal — in a real app you'd use a database.

### "Cannot withdraw Rs. X"
This means your business rules are working! Savings accounts must keep ₹1,000 minimum, and Current accounts can't go below -₹5,000.

---

## 11. Glossary

| Term | Meaning |
|------|---------|
| **API** | A set of rules/URLs for programs to communicate |
| **REST** | A style of API using HTTP methods (GET, POST, etc.) |
| **JSON** | A text format for data: `{"key": "value"}` |
| **HTTP** | The protocol browsers use to talk to servers |
| **GET** | HTTP method meaning "give me data" |
| **POST** | HTTP method meaning "here's data, process it" |
| **Endpoint** | A specific URL that does something (e.g., `/api/accounts`) |
| **Controller** | Java class that handles incoming web requests |
| **Service** | Java class that contains business logic |
| **DTO** | Simple class for receiving data from requests |
| **Maven** | Tool that downloads Java libraries (dependencies) |
| **pom.xml** | Maven's config file — lists what your project needs |
| **Tomcat** | Web server embedded inside Spring Boot |
| **Annotation** | `@` labels that give Spring Boot instructions (`@GetMapping`, etc.) |
| **Dependency Injection** | Spring creates and connects objects for you |
| **Abstract Class** | A class you can't instantiate — must be subclassed |
| **Polymorphism** | Same method, different behavior based on object type |
| **fetch()** | JavaScript function to make HTTP requests |
| **async/await** | JavaScript way to wait for network responses |
| **DOM** | The browser's internal representation of the HTML page |
| **Callback** | A function that runs when something happens (click, response, etc.) |
| **Glassmorphism** | UI design trend using frosted glass effects |
| **Responsive** | Design that adapts to different screen sizes |
| **Port** | A number (8080) identifying which app on a computer to talk to |
| **localhost** | "This computer" — used during development |

---

## 🎓 What to Learn Next

Now that you understand this project, here are natural next steps:

1. **Add a Database** — Use Spring Data JPA + H2/PostgreSQL so data persists after restart
2. **Add Authentication** — Spring Security for login/logout
3. **Add Validation** — Use `@Valid` annotations on DTOs
4. **Write Tests** — JUnit + MockMvc for testing API endpoints
5. **Dockerize** — Package the app in a Docker container
6. **Deploy** — Push to Railway, Render, or AWS

---

> **Remember:** Every expert was once a beginner. You just built a full-stack web application. That's a huge achievement! 🚀
