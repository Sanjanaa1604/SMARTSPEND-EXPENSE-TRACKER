package com.smartspend.controller;

import com.smartspend.analytics.FinancialAnalytics;
import com.smartspend.dao.TransactionDAO;
import com.smartspend.model.Transaction;
import com.smartspend.util.Session;
import com.smartspend.util.UI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DashboardView extends VBox {

    private final TransactionDAO dao =
            new TransactionDAO();

    private final FinancialAnalytics analytics =
            new FinancialAnalytics();

    private final int uid =
            Session.getUser().getId();

    private final HBox stats =
            new HBox(16);

    private final VBox recent =
            new VBox(10);

    private final ExecutorService pool =
            Executors.newFixedThreadPool(2);


    public DashboardView() {

        setFillWidth(true);

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(24, 32, 40, 32)
        );

        content.setFillWidth(true);


        /*
         * HEADER
         */

        VBox header =
                createHeader();


        /*
         * STAT CARDS
         */

        stats.setFillHeight(true);


        /*
         * QUICK ACTIONS
         */

        HBox quickActions =
                createQuickActions();


        /*
         * CHARTS
         */

        HBox charts =
                createCharts();


        /*
         * RECENT TRANSACTIONS
         */

        recent.getStyleClass()
                .add("dashboard-section");


        Label recentTitle =
                new Label("Recent Transactions");

        recentTitle.getStyleClass()
                .add("section-title");


        recent.getChildren()
                .add(recentTitle);


        /*
         * ADD EVERYTHING TO CONTENT
         */

        content.getChildren().addAll(
                header,
                stats,
                quickActions,
                charts,
                recent
        );


        /*
         * SCROLLPANE
         */

        ScrollPane scroll =
                new ScrollPane(content);

        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);

        scroll.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.NEVER
        );

        scroll.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scroll.setPannable(true);

        scroll.getStyleClass()
                .add("dashboard-scroll");


        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );


        getChildren().add(scroll);


        /*
         * LOAD DATA
         */

        load();
    }


    /*
     * ============================================================
     * HEADER
     * ============================================================
     */

    private VBox createHeader() {

        VBox box =
                new VBox(5);


        Label title =
                new Label("Good to see you 👋");

        title.getStyleClass()
                .add("page-title");


        Label subtitle =
                new Label(
                        "Here's your financial overview for this month."
                );

        subtitle.getStyleClass()
                .add("page-subtitle");


        box.getChildren()
                .addAll(
                        title,
                        subtitle
                );


        return box;
    }


    /*
     * ============================================================
     * QUICK ACTIONS
     * ============================================================
     */

    private HBox createQuickActions() {

        HBox box =
                new HBox(10);

        box.setAlignment(
                Pos.CENTER_LEFT
        );


        Label label =
                new Label("Quick Actions");

        label.getStyleClass()
                .add("section-title");


        Button addTransaction =
                UI.button("+ Add Transaction");


        Button viewTransactions =
                UI.ghost("View Transactions");


        /*
         * ADD TRANSACTION
         */

        addTransaction.setOnAction(e -> {

            if (getScene() == null) {
                return;
            }


            if (getScene().getRoot()
                    instanceof MainShell shell) {

                shell.openTransactionsAndAdd();
            }
        });


        /*
         * VIEW TRANSACTIONS
         */

        viewTransactions.setOnAction(e -> {

            if (getScene() == null) {
                return;
            }


            if (getScene().getRoot()
                    instanceof MainShell shell) {

                shell.showPage("Transactions");
            }
        });


        box.getChildren()
                .addAll(
                        label,
                        addTransaction,
                        viewTransactions
                );


        return box;
    }


    /*
     * ============================================================
     * CHART CONTAINER
     * ============================================================
     */

    private HBox createCharts() {

        HBox charts =
                new HBox(16);

        charts.setFillHeight(true);


        VBox monthly =
                new VBox(10);

        monthly.getStyleClass()
                .add("dashboard-section");


        VBox categories =
                new VBox(10);

        categories.getStyleClass()
                .add("dashboard-section");


        Label monthlyTitle =
                new Label("Monthly Overview");

        monthlyTitle.getStyleClass()
                .add("section-title");


        Label categoryTitle =
                new Label("Spending by Category");

        categoryTitle.getStyleClass()
                .add("section-title");


        monthly.getChildren()
                .add(monthlyTitle);

        categories.getChildren()
                .add(categoryTitle);


        HBox.setHgrow(
                monthly,
                Priority.ALWAYS
        );

        HBox.setHgrow(
                categories,
                Priority.ALWAYS
        );


        charts.getChildren()
                .addAll(
                        monthly,
                        categories
                );


        /*
         * Charts will be filled by load()
         */

        return charts;
    }


    /*
     * ============================================================
     * LOAD DATA
     * ============================================================
     */

    private void load() {

        pool.submit(() -> {

            try {

                List<Transaction> transactions =
                        dao.findByUser(uid);


                Platform.runLater(() ->
                        updateDashboard(transactions)
                );


            } catch (Exception e) {

                Platform.runLater(() -> {

                    recent.getChildren()
                            .clear();

                    Label error =
                            new Label(
                                    "Unable to load dashboard data."
                            );

                    error.getStyleClass()
                            .add("muted");

                    recent.getChildren()
                            .add(error);
                });
            }
        });
    }


    /*
     * ============================================================
     * UPDATE DASHBOARD
     * ============================================================
     */

    private void updateDashboard(
            List<Transaction> transactions
    ) {

        /*
         * Clear old dynamic content.
         */

        stats.getChildren()
                .clear();


        /*
         * Calculate totals.
         */

        double income =
                analytics.total(
                        transactions,
                        "INCOME"
                );


        double expense =
                analytics.total(
                        transactions,
                        "EXPENSE"
                );


        double balance =
                income - expense;


        double savingsRate =
                analytics.savingsRate(
                        income,
                        expense
                );


        /*
         * STAT CARDS
         */

        VBox incomeCard =
                createStatCard(
                        "Total Income",
                        UI.money(income),
                        "This month's income"
                );


        VBox expenseCard =
                createStatCard(
                        "Total Expenses",
                        UI.money(expense),
                        "This month's spending"
                );


        VBox balanceCard =
                createStatCard(
                        "Balance",
                        UI.money(balance),
                        "Income - Expenses"
                );


        VBox savingsCard =
                createStatCard(
                        "Savings Rate",
                        String.format(
                                "%.1f%%",
                                savingsRate
                        ),
                        "Money retained"
                );


        stats.getChildren()
                .addAll(
                        incomeCard,
                        expenseCard,
                        balanceCard,
                        savingsCard
                );


        for (javafx.scene.Node node :
                stats.getChildren()) {

            HBox.setHgrow(
                    node,
                    Priority.ALWAYS
            );
        }


        /*
         * UPDATE CHARTS
         */

        updateCharts(transactions);


        /*
         * UPDATE RECENT TRANSACTIONS
         */

        updateRecent(transactions);


        /*
         * Empty-state hint
         */

        if (transactions.isEmpty()) {

            addEmptyDashboardMessage();
        }
    }


    /*
     * ============================================================
     * STAT CARD
     * ============================================================
     */

    private VBox createStatCard(
            String title,
            String value,
            String description
    ) {

        VBox card =
                UI.card();


        Label titleLabel =
                new Label(title);

        titleLabel.getStyleClass()
                .add("muted");


        Label valueLabel =
                new Label(value);

        valueLabel.getStyleClass()
                .add("stat-value");


        Label descriptionLabel =
                new Label(description);

        descriptionLabel.getStyleClass()
                .add("muted");


        card.getChildren()
                .addAll(
                        titleLabel,
                        valueLabel,
                        descriptionLabel
                );


        HBox.setHgrow(
                card,
                Priority.ALWAYS
        );


        return card;
    }


    /*
     * ============================================================
     * CHARTS
     * ============================================================
     */

    private void updateCharts(
            List<Transaction> transactions
    ) {

        /*
         * Find chart containers.
         */

        if (getChildren().isEmpty()) {
            return;
        }


        /*
         * Instead of relying on child indexes,
         * locate the ScrollPane content.
         */

        if (!(getChildren().get(0)
                instanceof ScrollPane scroll)) {

            return;
        }


        if (!(scroll.getContent()
                instanceof VBox content)) {

            return;
        }


        HBox charts = null;


        for (javafx.scene.Node node :
                content.getChildren()) {

            if (node instanceof HBox h &&
                    h.getChildren().size() == 2) {

                charts = h;
                break;
            }
        }


        if (charts == null) {
            return;
        }


        VBox monthly =
                (VBox) charts.getChildren().get(0);

        VBox categories =
                (VBox) charts.getChildren().get(1);


        /*
         * Remove old chart contents,
         * keeping title labels.
         */

        while (monthly.getChildren().size() > 1) {

            monthly.getChildren()
                    .remove(
                            monthly.getChildren().size() - 1
                    );
        }


        while (categories.getChildren().size() > 1) {

            categories.getChildren()
                    .remove(
                            categories.getChildren().size() - 1
                    );
        }


        /*
         * EMPTY STATE
         */

        if (transactions.isEmpty()) {

            Label emptyMonthly =
                    new Label(
                            "No transaction data yet.\n"
                                    + "Add your first transaction."
                    );

            emptyMonthly.setWrapText(true);

            emptyMonthly.getStyleClass()
                    .add("muted");


            Label emptyCategory =
                    new Label(
                            "Your spending categories\n"
                                    + "will appear here."
                    );

            emptyCategory.setWrapText(true);

            emptyCategory.getStyleClass()
                    .add("muted");


            monthly.getChildren()
                    .add(emptyMonthly);

            categories.getChildren()
                    .add(emptyCategory);

            return;
        }


        /*
         * Current month
         */

        LocalDate now =
                LocalDate.now();


        Map<String, Double> monthlyTotals =
                dao.monthlyTotals(
                        uid,
                        now.getYear(),
                        now.getMonthValue()
                );


        Map<String, Double> categoryTotals =
                dao.categoryTotals(
                        uid,
                        now.getYear(),
                        now.getMonthValue()
                );


        /*
         * BAR CHART
         */

        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();


        BarChart<String, Number> barChart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );


        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setMinHeight(260);
        barChart.setTitle("Income vs Expenses");


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        series.getData()
                .add(
                        new XYChart.Data<>(
                                "Income",
                                monthlyTotals.getOrDefault(
                                        "INCOME",
                                        0.0
                                )
                        )
                );


        series.getData()
                .add(
                        new XYChart.Data<>(
                                "Expenses",
                                monthlyTotals.getOrDefault(
                                        "EXPENSE",
                                        0.0
                                )
                        )
                );


        barChart.getData()
                .add(series);


        monthly.getChildren()
                .add(barChart);


        /*
         * PIE CHART
         */

        if (categoryTotals.isEmpty()) {

            Label empty =
                    new Label(
                            "No expenses recorded this month."
                    );

            empty.getStyleClass()
                    .add("muted");

            categories.getChildren()
                    .add(empty);

            return;
        }


        PieChart pie =
                new PieChart();


        pie.setTitle(
                "Expense Categories"
        );

        pie.setLegendVisible(true);

        pie.setLabelsVisible(true);

        pie.setMinHeight(260);


        for (
                Map.Entry<String, Double> entry :
                categoryTotals.entrySet()
        ) {

            pie.getData()
                    .add(
                            new PieChart.Data(
                                    entry.getKey(),
                                    entry.getValue()
                            )
                    );
        }


        categories.getChildren()
                .add(pie);
    }


    /*
     * ============================================================
     * RECENT TRANSACTIONS
     * ============================================================
     */

    private void updateRecent(
            List<Transaction> transactions
    ) {

        recent.getChildren()
                .clear();


        Label title =
                new Label("Recent Transactions");

        title.getStyleClass()
                .add("section-title");


        recent.getChildren()
                .add(title);


        if (transactions.isEmpty()) {

            Label empty =
                    new Label(
                            "No transactions yet."
                    );

            empty.getStyleClass()
                    .add("muted");


            Button add =
                    UI.button(
                            "+ Add Your First Transaction"
                    );


            add.setOnAction(e -> {

                if (getScene() != null &&
                        getScene().getRoot()
                                instanceof MainShell shell) {

                    shell.openTransactionsAndAdd();
                }
            });


            recent.getChildren()
                    .addAll(
                            empty,
                            add
                    );

            return;
        }


        int count =
                Math.min(
                        6,
                        transactions.size()
                );


        for (int i = 0; i < count; i++) {

            Transaction t =
                    transactions.get(i);


            recent.getChildren()
                    .add(
                            createTransactionRow(t)
                    );
        }
    }


    /*
     * ============================================================
     * TRANSACTION ROW
     * ============================================================
     */

    private HBox createTransactionRow(
            Transaction t
    ) {

        HBox row =
                new HBox(12);

        row.setAlignment(
                Pos.CENTER_LEFT
        );

        row.setPadding(
                new Insets(12)
        );


        Label icon =
                new Label(
                        getCategoryIcon(
                                t.getCategoryName()
                        )
                );

        icon.getStyleClass()
                .add("transaction-icon");


        VBox information =
                new VBox(3);


        Label description =
                new Label(
                        t.getDescription()
                );

        description.getStyleClass()
                .add("transaction-title");


        Label details =
                new Label(
                        t.getCategoryName()
                                + " • "
                                + t.getPaymentMethod()
                                + " • "
                                + t.getTransactionDate()
                );

        details.getStyleClass()
                .add("muted");


        information.getChildren()
                .addAll(
                        description,
                        details
                );


        Region spacer =
                new Region();

        HBox.setHgrow(
                spacer,
                Priority.ALWAYS
        );


        String amountText =
                (t.getType().equals("INCOME")
                        ? "+ "
                        : "- ")
                        + UI.money(
                                t.getAmount()
                        );


        Label amount =
                new Label(amountText);


        amount.getStyleClass()
                .add(
                        t.getType().equals("INCOME")
                                ? "income-amount"
                                : "expense-amount"
                );


        row.getChildren()
                .addAll(
                        icon,
                        information,
                        spacer,
                        amount
                );


        return row;
    }


    /*
     * ============================================================
     * CATEGORY ICONS
     * ============================================================
     */

    private String getCategoryIcon(
            String category
    ) {

        if (category == null) {
            return "💰";
        }


        String c =
                category.toLowerCase();


        if (c.contains("food") ||
                c.contains("restaurant")) {

            return "🍔";
        }


        if (c.contains("shopping")) {

            return "🛍";
        }


        if (c.contains("transport") ||
                c.contains("travel")) {

            return "🚗";
        }


        if (c.contains("education")) {

            return "📚";
        }


        if (c.contains("bill") ||
                c.contains("utility")) {

            return "💡";
        }


        if (c.contains("entertainment")) {

            return "🎬";
        }


        if (c.contains("salary") ||
                c.contains("income")) {

            return "💵";
        }


        return "💰";
    }


    /*
     * ============================================================
     * EMPTY DASHBOARD MESSAGE
     * ============================================================
     */

    private void addEmptyDashboardMessage() {

        Label message =
                new Label(
                        "Start by adding your first transaction. "
                                + "Your dashboard will automatically update "
                                + "as you track your finances."
                );

        message.setWrapText(true);

        message.getStyleClass()
                .add("muted");


        recent.getChildren()
                .add(message);
    }


    /*
     * ============================================================
     * CLEANUP
     * ============================================================
     */

    public void shutdown() {

        pool.shutdownNow();
    }
}