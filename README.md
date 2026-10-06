# SmartSpend — AI-Powered Personal Finance Manager

A Java 21 + JavaFX + MySQL + JDBC desktop fintech-style academic project.

## What is included
- Login and registration with PBKDF2 password hashing
- MySQL/JDBC persistence with prepared statements
- Dashboard with database-driven balance, income, expenses, savings rate and JavaFX charts
- Transaction CRUD, search, filters and sorting
- Monthly budgets and utilization status
- Savings goals with remaining amount, days and required monthly saving
- Recurring payments and due-date calculations
- Analytics using List, Map and transparent calculations
- Moving-average / projection classes for future expense prediction
- Statistical unusual-spending detector
- Rule-based AI insight fallback that works without an API key
- Receipt scanner interface with explicitly labeled demo parser
- Notifications
- CSV report export
- Dark fintech CSS theme and subtle page fade animation

## Technology
Java 21, JavaFX 21, MySQL 8+, JDBC, Java Collections, ExecutorService, CSS.

## Architecture
`controller -> service/analytics/ai -> dao -> JDBC -> MySQL`

Models contain application data. DAO classes contain SQL. Services contain validation/business rules. Analytics contains calculations. Controllers contain JavaFX UI logic.

## Windows setup — no Maven required

### 1. Java
Install JDK 21. Open **Command Prompt** and run:

```bat
java -version
javac -version
```

Both should report Java 21.

### 2. JavaFX
Download the JavaFX 21 Windows SDK from the official OpenJFX/Gluon distribution page. Extract it so this folder exists:

`SmartSpend\lib\javafx-sdk-21.0.8\lib\javafx.controls.jar`

The SDK's `lib` folder contains the JavaFX jars.

### 3. MySQL Connector/J
Download MySQL Connector/J 9.4.0 and copy the jar to:

`SmartSpend\lib\mysql-connector-j-9.4.0.jar`

### 4. MySQL database
Open **MySQL Workbench** or the MySQL command-line client. Run `sql/schema.sql` first.
Then run `sql/sample_data.sql`.

The sample account is:
- Email: `demo@smartspend.com`
- Password: `SmartSpend123`

If you have trouble with the sample account, simply use the Register screen to create a new account.

### 5. Database credentials
The application reads these environment variables:

`DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASSWORD`

If they are not set, defaults are localhost, 3306, smartspend, root and an empty password.

For a temporary Command Prompt session:

```bat
set DB_HOST=localhost
set DB_PORT=3306
set DB_NAME=smartspend
set DB_USER=root
set DB_PASSWORD=YOUR_MYSQL_PASSWORD
```

Run these commands in the **same Command Prompt** from the SmartSpend folder.

### 6. Run
Double-click `run.bat`, or open Command Prompt in the SmartSpend folder and run:

```bat
run.bat
```

PowerShell alternative:

```powershell
Set-ExecutionPolicy -Scope Process Bypass
.\run.ps1
```

## VS Code
Open the **SmartSpend folder**, not only the `src` folder. Java Extension Pack is recommended.

The project intentionally does not require Maven globally.

## Maven option
If you later want Maven, the project can be converted to a `pom.xml` build. The delivered setup is intentionally manual-library based so the app can be run without installing Maven.

## Database isolation
Every financial query includes `user_id`, and foreign keys cascade user-owned records. A logged-in user only queries their own rows.

## OOP concepts demonstrated
- Encapsulation: private model fields with getters/setters
- Constructors: model constructors
- Abstraction: `FinancialInsight` abstract class and `AIService` interface
- Inheritance: `SpendingInsight`, `SavingsInsight`, `BudgetInsight`
- Polymorphism: `List<FinancialInsight>` stores different insight subclasses
- Interfaces: `AIService`

## Collections
- `List<Transaction>` stores transaction records
- `Map<String,Double>` aggregates category spending
- `Set` can be added for unique categories when importing CSV; current analytics primarily demonstrates List/Map because those directly match the calculations.

## Multithreading
The dashboard loads transactions using `CompletableFuture`/`ExecutorService` so database/analytics work does not block the JavaFX UI thread. This is useful for a desktop app because slow database work should not freeze the interface.

## Prediction
`PredictionEngine` contains transparent moving-average, weighted-average and month-end projection methods. These are statistical calculations, not machine learning.

## Anomaly detection
`AnomalyDetector` compares a transaction with historical transactions in the same category and flags unusually large values using standard-deviation / multiplier rules. It is intentionally transparent rather than falsely labeled AI/ML.

## AI module
The default `RuleBasedAIService` consumes calculated facts and creates explanations. It does not invent financial numbers. An external AI provider can be plugged into the `AIService` interface later using an environment variable such as `AI_API_KEY`; no key is stored in source code.

## Receipt scanner
The UI requires review before saving. The included parser is explicitly a **demo parser**, because a production OCR engine requires a separately configured OCR dependency/provider. Replace `ReceiptService` with a real OCR implementation without changing the receipt UI contract.

## Financial health score
The analytics class contains an application-defined educational score based on savings, budget use, consistency, goal progress and recurring burden. It is not professional financial advice.

## Troubleshooting
### `javafx.controls.jar` not found
Check the JavaFX SDK folder name and make sure the SDK's `lib` directory is exactly under `lib\javafx-sdk-21.0.8\lib`.

### `ClassNotFoundException: com.mysql.cj.jdbc.Driver`
Check that `mysql-connector-j-9.4.0.jar` is in the `lib` directory with the exact name expected by `run.bat`.

### Communications link failure
Make sure MySQL Server is running and DB credentials are correct.

### Unknown database `smartspend`
Run `sql/schema.sql` first.

### Access denied for user
Set `DB_USER` and `DB_PASSWORD` correctly in the same terminal used to run the app.

## Project structure
```
SmartSpend/
├─ src/com/smartspend/
│  ├─ Main.java
│  ├─ model/
│  ├─ dao/
│  ├─ service/
│  ├─ analytics/
│  ├─ ai/
│  ├─ exception/
│  ├─ util/
│  └─ controller/
├─ resources/css/style.css
├─ sql/schema.sql
├─ sql/sample_data.sql
├─ lib/                     # put JavaFX SDK and MySQL Connector here
├─ run.bat
├─ run.ps1
├─ .env.example
└─ README.md
```

## Viva talking points
1. Why JDBC? Direct Java-to-MySQL connectivity with prepared statements.
2. Why DAO? SQL is separated from UI and business logic.
3. Why service layer? Validation and business rules stay out of controllers.
4. Why JavaFX? Native Java desktop UI with charts and CSS styling.
5. Why PBKDF2? Passwords are not stored as plaintext; PBKDF2 is a standard password-key-derivation approach available in the JDK.
6. Is the prediction ML? No. It is explicitly a transparent statistical projection.
7. Is the anomaly detector ML? No. It uses category history and a statistical threshold.
8. How is user isolation achieved? Every user-owned query is scoped by authenticated `user_id` and the schema uses foreign keys.
9. Where is multithreading? Dashboard database loading is performed off the JavaFX UI thread.
10. What happens without an AI API key? The local rule-based insight engine continues to work.
