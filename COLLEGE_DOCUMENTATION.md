# SmartSpend — College Project Quick Documentation

## Problem
Personal finance data is often scattered across notes, banking apps and reminders. SmartSpend centralizes income, expenses, budgets, goals and recurring payments in a desktop Java application.

## Objectives
1. Secure user authentication.
2. Persistent MySQL transaction management through JDBC.
3. Visual analytics using JavaFX charts.
4. Transparent prediction and anomaly detection.
5. Explainable AI-style insights with a local fallback.
6. Demonstrate OOP, collections, exceptions, file handling and multithreading.

## Modules
Authentication, Dashboard, Transactions, Budgets, Savings Goals, Recurring Payments, Analytics, AI Insights, Receipt Scanner, Notifications, Reports, Settings.

## Algorithms
- Moving average: mean of recent monthly expenses.
- Weighted average: newer months receive larger weights.
- Month-end projection: current spend / elapsed days × total days.
- Anomaly detection: category average plus standard-deviation threshold and multiplier rule.
- Financial health: weighted educational metric; not professional advice.

## OOP
`FinancialInsight` is abstract. `SpendingInsight`, `SavingsInsight`, and `BudgetInsight` inherit it. `AIService` is an interface, and `RuleBasedAIService` implements it. Model classes encapsulate fields.

## Multithreading
The dashboard loads database records using an `ExecutorService`, keeping JavaFX responsive.

## Security
Passwords use PBKDF2WithHmacSHA256. SQL uses `PreparedStatement`. User-owned queries are scoped by `user_id`. Credentials are environment variables rather than source-code constants.

## Viva demo flow
Register → login → dashboard → add expense → show transaction → show budget utilization → open Analytics → open AI Insights → open Receipt Scanner → generate CSV report.
