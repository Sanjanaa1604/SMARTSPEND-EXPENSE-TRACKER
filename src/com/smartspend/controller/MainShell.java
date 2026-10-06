package com.smartspend.controller;

import com.smartspend.service.NotificationService;
import com.smartspend.util.Session;
import com.smartspend.util.UI;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainShell extends BorderPane {

    private final Stage stage;

    private final StackPane content =
            new StackPane();

    private final Label page =
            new Label();


    public MainShell(Stage stage) {

        this.stage = stage;


        getStyleClass()
                .add("app");


        /*
         * SIDEBAR
         */

        setLeft(
                sidebar()
        );


        /*
         * TOP BAR
         */

        BorderPane top =
                new BorderPane();


        top.setPadding(
                new Insets(
                        22,
                        30,
                        18,
                        30
                )
        );


        page.getStyleClass()
                .add("top-title");


        top.setLeft(page);


        Label user =
                new Label(
                        Session.getUser().getName()
                                + "  •  ₹ INR"
                );


        user.getStyleClass()
                .add("user-chip");


        top.setRight(user);


        setTop(top);


        /*
         * MAIN CONTENT
         */

        setCenter(content);


        /*
         * NOTIFICATIONS
         */

        new NotificationService()
                .refresh(
                        Session.getUser().getId()
                );


        /*
         * OPEN DASHBOARD
         */

        show("Dashboard");
    }


    /*
     * ============================================================
     * SIDEBAR
     * ============================================================
     */

    private VBox sidebar() {

        VBox sidebar =
                new VBox(8);


        sidebar.setPadding(
                new Insets(
                        26,
                        16,
                        20,
                        16
                )
        );


        sidebar.setPrefWidth(225);


        sidebar.getStyleClass()
                .add("sidebar");


        /*
         * LOGO
         */

        Label logo =
                new Label(
                        "◆ SMARTSPEND"
                );

        logo.getStyleClass()
                .add("logo");


        Label subtitle =
                new Label(
                        "AI PERSONAL FINANCE"
                );

        subtitle.getStyleClass()
                .add("side-sub");


        sidebar.getChildren()
                .addAll(
                        logo,
                        subtitle
                );


        /*
         * NAVIGATION
         */

        String[] items = {

                "Dashboard",

                "Transactions",

                "Budgets",

                "Savings Goals",

                "Recurring Payments",

                "Analytics",

                "AI Insights",

                "Receipt Scanner",

                "Notifications",

                "Reports",

                "Settings"
        };


        for (String item : items) {

            Button button =
                    new Button(item);


            button.getStyleClass()
                    .add("nav-button");


            button.setMaxWidth(
                    Double.MAX_VALUE
            );


            button.setOnAction(
                    e -> show(item)
            );


            sidebar.getChildren()
                    .add(button);
        }


        /*
         * SPACER
         */

        Region spacer =
                new Region();


        VBox.setVgrow(
                spacer,
                Priority.ALWAYS
        );


        /*
         * LOGOUT
         */

        Button logout =
                UI.ghost("Log out");


        logout.setMaxWidth(
                Double.MAX_VALUE
        );


        logout.setOnAction(
                e -> logout()
        );


        sidebar.getChildren()
                .addAll(
                        spacer,
                        logout
                );


        return sidebar;
    }


    /*
     * ============================================================
     * PUBLIC NAVIGATION
     * ============================================================
     *
     * DashboardView uses these methods for Quick Actions.
     */

    public void showPage(
            String name
    ) {

        show(name);
    }


    /*
     * ============================================================
     * OPEN TRANSACTIONS + ADD
     * ============================================================
     */

    public void openTransactionsAndAdd() {

        TransactionsView view =
                new TransactionsView();


        page.setText(
                "Transactions"
        );


        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(220),
                        view
                );


        fade.setFromValue(0);

        fade.setToValue(1);


        content.getChildren()
                .setAll(view);


        fade.play();


        /*
         * Open the Add Transaction dialog
         */

        view.openAddTransaction();
    }


    /*
     * ============================================================
     * SHOW PAGE
     * ============================================================
     */

    private void show(
            String name
    ) {

        page.setText(name);


        Node view =
                switch (name) {

                    case "Dashboard" ->
                            new DashboardView();


                    case "Transactions" ->
                            new TransactionsView();


                    case "Budgets" ->
                            new BudgetsView();


                    case "Savings Goals" ->
                            new SavingsView();


                    case "Recurring Payments" ->
                            new RecurringView();


                    case "Analytics" ->
                            new AnalyticsView();


                    case "AI Insights" ->
                            new AIView();


                    case "Receipt Scanner" ->
                            new ReceiptView();


                    case "Notifications" ->
                            new NotificationsView();


                    case "Reports" ->
                            new ReportsView();


                    case "Settings" ->
                            new SettingsView(stage);


                    default ->
                            new DashboardView();
                };


        /*
         * Fade animation
         */

        FadeTransition fade =
                new FadeTransition(
                        Duration.millis(220),
                        view
                );


        fade.setFromValue(0);

        fade.setToValue(1);


        /*
         * Replace current page
         */

        content.getChildren()
                .setAll(view);


        fade.play();
    }


    /*
     * ============================================================
     * LOGOUT
     * ============================================================
     */

    private void logout() {

        Session.clear();


        stage.getScene()
                .setRoot(
                        new LoginView(
                                stage,
                                () ->
                                        stage.getScene()
                                                .setRoot(
                                                        new RegisterView(
                                                                stage,
                                                                () ->
                                                                        stage.getScene()
                                                                                .setRoot(
                                                                                        new LoginView(
                                                                                                stage,
                                                                                                () -> {}
                                                                                        )
                                                                                )
                                                        )
                                                )
                        )
                );
    }
}