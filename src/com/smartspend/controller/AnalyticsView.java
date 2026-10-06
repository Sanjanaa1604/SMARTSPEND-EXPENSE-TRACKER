package com.smartspend.controller;

import com.smartspend.analytics.*;
import com.smartspend.dao.TransactionDAO;
import com.smartspend.model.Transaction;
import com.smartspend.util.*;

import javafx.geometry.*;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class AnalyticsView extends VBox {

    private final TransactionDAO dao =
            new TransactionDAO();

    private final FinancialAnalytics a =
            new FinancialAnalytics();

    private final int uid =
            Session.getUser().getId();


    public AnalyticsView() {

        setPadding(
                new Insets(5, 30, 30, 30)
        );

        setSpacing(15);


        List<Transaction> t =
                dao.findByUser(uid);


        getChildren().addAll(
                UI.title("Analytics"),
                UI.subtitle(
                        "Transparent calculations from your transaction history."
                )
        );


        if (t.isEmpty()) {

            showEmptyState();

            return;
        }


        buildAnalytics(t);
    }


    private void showEmptyState() {

        VBox empty =
                UI.card();


        Label icon =
                new Label("📊");

        icon.setStyle(
                "-fx-font-size: 42px;"
        );


        Label title =
                new Label(
                        "No analytics data yet"
                );

        title.getStyleClass()
                .add("section-title");


        Label message =
                new Label(
                        "Add your first income or expense transaction "
                                + "to start seeing your financial trends."
                );

        message.setWrapText(true);

        message.getStyleClass()
                .add("muted");


        Button add =
                UI.button("+ Add Transaction");


        add.setOnAction(
                e -> {

                    if (getScene() != null &&
                            getScene().getRoot()
                                    instanceof MainShell shell) {

                        shell.openTransactionsAndAdd();
                    }
                }
        );


        empty.setAlignment(
                Pos.CENTER
        );

        empty.setSpacing(12);

        empty.setPadding(
                new Insets(50)
        );


        empty.getChildren().addAll(
                icon,
                title,
                message,
                add
        );


        getChildren().add(
                empty
        );
    }


    private void buildAnalytics(
            List<Transaction> t
    ) {

        double income =
                a.total(
                        t,
                        "INCOME"
                );


        double expense =
                a.total(
                        t,
                        "EXPENSE"
                );


        Map<String, Double> cats =
                a.byCategory(t);


        String top =
                a.highestCategory(cats);


        GridPane cards =
                new GridPane();

        cards.setHgap(12);
        cards.setVgap(12);


        cards.add(
                UI.card(
                        UI.subtitle("Total spending"),
                        new Label(
                                UI.money(expense)
                        )
                ),
                0,
                0
        );


        cards.add(
                UI.card(
                        UI.subtitle("Average daily"),
                        new Label(
                                UI.money(
                                        a.averageDaily(t)
                                )
                        )
                ),
                1,
                0
        );


        cards.add(
                UI.card(
                        UI.subtitle("Savings rate"),
                        new Label(
                                String.format(
                                        "%.1f%%",
                                        a.savingsRate(
                                                income,
                                                expense
                                        )
                                )
                        )
                ),
                2,
                0
        );


        cards.add(
                UI.card(
                        UI.subtitle("Highest category"),
                        new Label(
                                top
                        )
                ),
                3,
                0
        );


        getChildren().add(
                cards
        );


        CategoryAxis xAxis =
                new CategoryAxis();

        NumberAxis yAxis =
                new NumberAxis();


        BarChart<String, Number> chart =
                new BarChart<>(
                        xAxis,
                        yAxis
                );


        chart.setTitle(
                "Category Spending"
        );

        chart.setLegendVisible(false);

        chart.setAnimated(false);

        chart.setMinHeight(350);


        XYChart.Series<String, Number> series =
                new XYChart.Series<>();


        cats.forEach(
                (k, v) ->
                        series.getData().add(
                                new XYChart.Data<>(
                                        k,
                                        v
                                )
                        )
        );


        chart.getData()
                .add(series);


        getChildren().add(
                UI.card(chart)
        );


        Label weekend =
                new Label(
                        "Weekend spending: "
                                + String.format(
                                        "%.1f%%",
                                        a.weekendPercentage(t)
                                )
                );


        Label collections =
                new Label(
                        "Collections used: List<Transaction> "
                                + "for records and Map<String,Double> "
                                + "for category aggregation."
                );


        collections.getStyleClass()
                .add("muted");


        getChildren().add(
                UI.card(
                        weekend,
                        collections
                )
        );
    }
}