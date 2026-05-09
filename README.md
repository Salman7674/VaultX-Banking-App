# 🏦 VaultX — Full-Stack Banking Application

A complete banking application built with **Java (Spring Boot)** backend and **HTML/CSS/JavaScript** frontend. This project demonstrates how a real-world web application works — from button clicks in the browser all the way to Java code running on the server.

---

## 🚀 How to Run

```bash
cd /Users/salman/Downloads/BankingApp
./mvnw spring-boot:run
```

Then open **http://localhost:8080** in your browser. That's it!

> **Note:** You don't need Maven installed. The `mvnw` (Maven Wrapper) file downloads everything automatically.

---

## 📁 Project Structure

```
BankingApp/
├── pom.xml                              ← Maven config (like a recipe for building the app)
├── mvnw                                 ← Maven wrapper (runs Maven without installing it)
│
├── src/main/java/com/vaultx/banking/    ← ☕ JAVA BACKEND
│   ├── BankingApplication.java          ← App starts here (main method)
│   ├── controller/
│   │   └── BankController.java          ← 🌐 REST API endpoints (the "waiter")
│   ├── service/
│   │   └── BankService.java             ← 🧠 Business logic (the "chef")
│   ├── account/
│   │   ├── BankAccount.java             ← Abstract parent class
│   │   ├── SavingsAccount.java          ← Savings rules (min balance ₹1000)
│   │   └── CurrentAccount.java          ← Current rules (overdraft ₹5000)
│   ├── model/
│   │   └── Transaction.java             ← Transaction data model
│   ├── dto/
│   │   ├── CreateAccountRequest.java    ← What frontend sends to create account
│   │   ├── AmountRequest.java           ← What frontend sends for deposit/withdraw
│   │   └── TransferRequest.java         ← What frontend sends for transfer
│   └── exception/
│       ├── InvalidAmountException.java
│       ├── InsufficientFundsException.java
│       └── GlobalExceptionHandler.java  ← Catches errors, sends clean messages
│
├── src/main/resources/
│   ├── application.properties           ← App settings (port 8080)
│   └── static/                          ← 🎨 FRONTEND (served by Spring Boot)
│       ├── index.html                   ← Page structure
│       ├── styles.css                   ← Visual design
│       └── app.js                       ← Frontend logic (fetch calls to API)
│
└── src/                                 ← Original standalone Java files (kept as reference)
    ├── Main.java
    ├── account/, model/, exception/, service/
```

---

## 🔗 How Frontend Connects to Backend

This is the **most important** concept. Here's how it works in plain English:

### The Restaurant Analogy 🍽️

Think of a restaurant:

| Restaurant | Our App |
|---|---|
| **You (customer)** | The browser (Chrome) |
| **Menu** | The frontend (HTML page with buttons) |
| **Waiter** | `BankController.java` (REST API) |
| **Chef** | `BankService.java` (business logic) |
| **Kitchen ingredients** | `BankAccount.java`, `Transaction.java` (data) |
| **Your food** | JSON response (data sent back to browser) |

**You don't go into the kitchen.** You tell the waiter what you want, and the waiter brings it to you. Similarly, the **browser never touches Java directly** — it talks through the API.

### Step-by-Step: What Happens When You Click "Deposit"

```
1. You click "Deposit ₹5000" on the webpage
         ↓
2. JavaScript (app.js) sends an HTTP request:
   POST http://localhost:8080/api/accounts/SAV1001/deposit
   Body: { "amount": 5000 }
         ↓
3. Spring Boot receives this at BankController.java
   The @PostMapping("/accounts/{accNo}/deposit") method runs
         ↓
4. Controller calls BankService.deposit("SAV1001", 5000)
         ↓
5. BankService finds the account, calls account.deposit(5000)
         ↓
6. SavingsAccount.deposit() adds ₹5000 to balance,
   creates a Transaction record
         ↓
7. Controller converts the updated account to JSON:
   { "accountNumber": "SAV1001", "balance": 19500.00, ... }
         ↓
8. JSON travels back to the browser
         ↓
9. JavaScript (app.js) reads the response and updates the page
   The balance number changes on screen!
```

### The Key Technology: REST API

A **REST API** is just a set of URLs that accept and return data (JSON).

Instead of returning a webpage (HTML), these URLs return **raw data**:

```
GET  /api/accounts         →  Returns list of all accounts as JSON
POST /api/accounts          →  Creates a new account, returns it as JSON
POST /api/accounts/SAV1001/deposit  →  Deposits money, returns updated account
```

The frontend calls these URLs using JavaScript's `fetch()` function:

```javascript
// This is how the frontend talks to the backend
const response = await fetch('/api/accounts/SAV1001/deposit', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ amount: 5000 })
});
const updatedAccount = await response.json();
// Now we have the data from Java, we can show it on screen!
```

---

## 🛠️ API Endpoints

| Method | URL | What it does |
|--------|-----|-------------|
| `GET` | `/api/stats` | Get dashboard numbers (total balance, counts) |
| `GET` | `/api/accounts` | List all bank accounts |
| `GET` | `/api/accounts/SAV1001` | Get one account with its transactions |
| `POST` | `/api/accounts` | Create a new account |
| `POST` | `/api/accounts/SAV1001/deposit` | Deposit money |
| `POST` | `/api/accounts/SAV1001/withdraw` | Withdraw money |
| `POST` | `/api/transfer` | Transfer money between accounts |
| `GET` | `/api/transactions` | Get all transactions (with filters) |

**Try it yourself!** While the app is running, open a new terminal and run:

```bash
# Get all accounts
curl http://localhost:8080/api/accounts

# Get dashboard stats
curl http://localhost:8080/api/stats

# Create a new account
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"holderName":"Test User","accountType":"savings","initialDeposit":5000}'
```

---

## 🧩 Tech Stack

| Layer | Technology | Why |
|-------|-----------|-----|
| **Backend** | Java 21 + Spring Boot 3.4 | Industry standard for enterprise apps |
| **API** | REST (JSON over HTTP) | Universal way for frontend ↔ backend communication |
| **Frontend** | HTML + CSS + Vanilla JavaScript | Simple, no framework overhead |
| **Build Tool** | Maven (via wrapper) | Manages Java dependencies |
| **Server** | Embedded Tomcat | Comes built-in with Spring Boot |

---

## ✨ Features

- **Dashboard** — Total balance, account counts, recent transactions
- **Account Management** — Create Savings & Current accounts
- **Deposits & Withdrawals** — With real validation (min balance, overdraft)
- **Fund Transfers** — Move money between accounts
- **Transaction History** — Filterable by account and type
- **Dark/Light Theme** — Toggle with the ☀️/🌙 button
- **Responsive Design** — Works on mobile screens too

---

## 📚 Want to Learn More?

Read the **[project.md](./project.md)** file — it's a complete beginner's guide that explains every concept used in this project, from what Java classes are to how Spring Boot works. Written in plain English!
