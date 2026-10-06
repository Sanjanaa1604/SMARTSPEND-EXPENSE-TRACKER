package com.smartspend.controller;

import com.smartspend.ai.FinancialInsight;
import com.smartspend.ai.InsightContext;
import com.smartspend.ai.RuleBasedAIService;
import com.smartspend.analytics.FinancialAnalytics;
import com.smartspend.dao.BudgetDAO;
import com.smartspend.dao.TransactionDAO;
import com.smartspend.model.Budget;
import com.smartspend.model.Transaction;
import com.smartspend.util.Session;
import com.smartspend.util.UI;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class AIView extends VBox {

    public AIView() {

        setPadding(new Insets(5, 30, 30, 30));
        setSpacing(14);

        int userId = Session.getUser().getId();

        // Get transactions for the logged-in user
        List<Transaction> transactions =
                new TransactionDAO().findByUser(userId);

        // Financial calculations
        FinancialAnalytics analytics =
                new FinancialAnalytics();

        double income =
                analytics.total(transactions, "INCOME");

        double expense =
                analytics.total(transactions, "EXPENSE");

        // Category analysis
        Map<String, Double> categoryAmounts =
                analytics.byCategory(transactions);

        String topCategory =
                analytics.highestCategory(categoryAmounts);

        double topCategoryAmount =
                categoryAmounts.getOrDefault(topCategory, 0.0);

        // Budget usage
        double budgetUsage = 0;

        List<Budget> budgets =
                new BudgetDAO().findForMonth(
                        userId,
                        LocalDate.now().getMonthValue(),
                        LocalDate.now().getYear()
                );

        for (Budget budget : budgets) {

            budgetUsage =
                    Math.max(
                            budgetUsage,
                            budget.getPercentage()
                    );
        }

        // Build AI context
        InsightContext context =
                new InsightContext(
                        income,
                        expense,
                        analytics.savingsRate(income, expense),
                        topCategory,
                        topCategoryAmount,
                        budgetUsage,
                        expense
                );

        // Page heading
        getChildren().addAll(
                UI.title("AI Insights"),
                UI.subtitle(
                        "SmartSpend AI uses calculated facts first, "
                        + "then generates explanations. "
                        + "Current mode: local rule-based fallback."
                )
        );

        // Generate financial insights
        List<FinancialInsight> insights =
                new RuleBasedAIService().generate(context);

        for (FinancialInsight insight : insights) {

            getChildren().add(
                    UI.card(
                            new Label(
                                    insight.icon()
                                            + "  "
                                            + insight.getTitle()
                            ),
                            new Label(
                                    insight.getMessage()
                            )
                    )
            );
        }

        // Transparency section
        getChildren().add(
                UI.card(
                        UI.subtitle("Transparency"),
                        new Label(
                                "No invented financial figures. "
                                + "This screen is generated from "
                                + "the database summary and uses "
                                + "rules rather than claiming "
                                + "machine learning."
                        )
                )
        );
    }
}