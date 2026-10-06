package com.smartspend.controller;

import com.smartspend.dao.CategoryDAO;
import com.smartspend.model.Category;
import com.smartspend.model.Transaction;
import com.smartspend.service.TransactionService;
import com.smartspend.util.Session;
import com.smartspend.util.UI;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;
import java.util.List;

public class TransactionsView extends VBox {

    private final TransactionService service =
            new TransactionService();

    private final List<Category> cats =
            new CategoryDAO().findAll();

    private final TextField search =
            UI.input("Search description, category or notes...");

    private final ComboBox<String> type =
            new ComboBox<>();

    private final ComboBox<String> cat =
            new ComboBox<>();

    private final ComboBox<String> sort =
            new ComboBox<>();

    private final TableView<Transaction> table =
            new TableView<>();

    private final int uid =
            Session.getUser().getId();


    public TransactionsView() {

        setPadding(
                new Insets(5, 30, 30, 30)
        );

        setSpacing(14);


        /*
         * =====================================================
         * FILTER BAR
         * =====================================================
         */

        HBox filters =
                new HBox(10);


        /*
         * TYPE FILTER
         */

        type.getItems().addAll(
                "All",
                "INCOME",
                "EXPENSE"
        );

        type.setValue("All");


        /*
         * CATEGORY FILTER
         */

        cat.getItems().add("All");

        for (Category c : cats) {

            cat.getItems()
                    .add(c.getName());
        }

        cat.setValue("All");


        /*
         * SORT
         */

        sort.getItems().addAll(
                "Newest",
                "Oldest",
                "Highest amount",
                "Lowest amount"
        );

        sort.setValue("Newest");


        /*
         * SEARCH
         */

        search.setPrefWidth(330);


        /*
         * BUTTONS
         */

        Button apply =
                UI.ghost("Apply Filters");

        apply.setOnAction(
                e -> load()
        );


        Button add =
                UI.button("+ Add Transaction");

        add.setOnAction(
                e -> openAddTransaction()
        );


        filters.getChildren()
                .addAll(
                        search,
                        type,
                        cat,
                        sort,
                        apply,
                        add
                );


        /*
         * =====================================================
         * TABLE
         * =====================================================
         */

        setupTable();


        /*
         * =====================================================
         * PAGE
         * =====================================================
         */

        getChildren().addAll(
                UI.title("Transactions"),
                UI.subtitle(
                        "Create, edit, search and filter every financial record."
                ),
                filters,
                table
        );


        VBox.setVgrow(
                table,
                Priority.ALWAYS
        );


        /*
         * LOAD DATA
         */

        load();
    }


    /*
     * =========================================================
     * DASHBOARD QUICK ACTION
     * =========================================================
     *
     * MainShell calls this method.
     */

    public void openAddTransaction() {

        dialog(null);
    }


    /*
     * =========================================================
     * TABLE SETUP
     * =========================================================
     */

    private void setupTable() {

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );


        /*
         * DATE
         */

        TableColumn<Transaction, String> dateColumn =
                new TableColumn<>("Date");

        dateColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue()
                                        .getTransactionDate()
                                        .toString()
                        )
        );


        /*
         * DESCRIPTION
         */

        TableColumn<Transaction, String> descriptionColumn =
                new TableColumn<>("Description");

        descriptionColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue()
                                        .getDescription()
                        )
        );


        /*
         * CATEGORY
         */

        TableColumn<Transaction, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue()
                                        .getCategoryName()
                        )
        );


        /*
         * PAYMENT METHOD
         */

        TableColumn<Transaction, String> methodColumn =
                new TableColumn<>("Method");

        methodColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue()
                                        .getPaymentMethod()
                        )
        );


        /*
         * TYPE
         */

        TableColumn<Transaction, String> typeColumn =
                new TableColumn<>("Type");

        typeColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                data.getValue()
                                        .getType()
                        )
        );


        /*
         * AMOUNT
         */

        TableColumn<Transaction, String> amountColumn =
                new TableColumn<>("Amount");

        amountColumn.setCellValueFactory(
                data ->
                        new javafx.beans.property.SimpleStringProperty(
                                UI.money(
                                        data.getValue()
                                                .getAmount()
                                )
                        )
        );


        /*
         * ACTIONS
         */

        TableColumn<Transaction, Void> actionColumn =
                new TableColumn<>("Actions");


        actionColumn.setCellFactory(
                column ->
                        new TableCell<>() {

                            private final Button edit =
                                    UI.ghost("Edit");

                            private final Button delete =
                                    UI.ghost("Delete");


                            {

                                delete.getStyleClass()
                                        .add("danger-button");


                                edit.setOnAction(
                                        e -> {

                                            Transaction t =
                                                    getTableView()
                                                            .getItems()
                                                            .get(getIndex());

                                            dialog(t);
                                        }
                                );


                                delete.setOnAction(
                                        e -> {

                                            Transaction t =
                                                    getTableView()
                                                            .getItems()
                                                            .get(getIndex());

                                            deleteTransaction(t);
                                        }
                                );
                            }


                            @Override
                            protected void updateItem(
                                    Void item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );


                                if (empty) {

                                    setGraphic(null);

                                } else {

                                    HBox buttons =
                                            new HBox(
                                                    5,
                                                    edit,
                                                    delete
                                            );

                                    setGraphic(buttons);
                                }
                            }
                        }
        );


        table.getColumns()
                .addAll(
                        dateColumn,
                        descriptionColumn,
                        categoryColumn,
                        methodColumn,
                        typeColumn,
                        amountColumn,
                        actionColumn
                );
    }


    /*
     * =========================================================
     * DELETE TRANSACTION
     * =========================================================
     */

    private void deleteTransaction(
            Transaction t
    ) {

        Alert confirm =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );


        confirm.setTitle(
                "Delete Transaction"
        );


        confirm.setHeaderText(
                "Delete this transaction?"
        );


        confirm.setContentText(
                t.getDescription()
                        + " • "
                        + UI.money(
                                t.getAmount()
                        )
        );


        confirm.showAndWait()
                .ifPresent(result -> {

                    if (result ==
                            ButtonType.OK) {

                        try {

                            service.delete(
                                    t.getId(),
                                    uid
                            );

                            load();

                        } catch (Exception e) {

                            UI.error(
                                    getScene().getWindow(),
                                    e.getMessage()
                            );
                        }
                    }
                });
    }


    /*
     * =========================================================
     * LOAD / FILTER
     * =========================================================
     */

    private void load() {

        String selectedSort =
                sort.getValue();


        String sortValue;


        if (selectedSort == null) {

            sortValue = "newest";

        } else if (
                selectedSort
                        .toLowerCase()
                        .startsWith("old")
        ) {

            sortValue = "oldest";

        } else if (
                selectedSort
                        .toLowerCase()
                        .startsWith("highest")
        ) {

            sortValue = "highest";

        } else if (
                selectedSort
                        .toLowerCase()
                        .startsWith("lowest")
        ) {

            sortValue = "lowest";

        } else {

            sortValue = "newest";
        }


        List<Transaction> results =
                service.search(
                        uid,
                        search.getText(),
                        type.getValue(),
                        cat.getValue(),
                        null,
                        null,
                        null,
                        null,
                        sortValue
                );


        table.getItems()
                .setAll(results);
    }


    /*
     * =========================================================
     * ADD / EDIT DIALOG
     * =========================================================
     */

    private void dialog(
            Transaction old
    ) {

        Dialog<ButtonType> d =
                new Dialog<>();


        d.setTitle(
                old == null
                        ? "Add Transaction"
                        : "Edit Transaction"
        );


        GridPane g =
                new GridPane();

        g.setHgap(10);
        g.setVgap(10);

        g.setPadding(
                new Insets(15)
        );


        /*
         * TYPE
         */

        ComboBox<String> t =
                new ComboBox<>();

        t.getItems().addAll(
                "INCOME",
                "EXPENSE"
        );


        t.setValue(
                old == null
                        ? "EXPENSE"
                        : old.getType()
        );


        /*
         * AMOUNT
         */

        TextField amount =
                UI.input("Amount");


        /*
         * DESCRIPTION
         */

        TextField desc =
                UI.input("Description");


        /*
         * NOTES
         */

        TextField notes =
                UI.input("Notes");


        /*
         * CATEGORY
         */

        ComboBox<Category> c =
                new ComboBox<>();


        c.getItems()
                .addAll(cats);


        /*
         * DATE
         */

        DatePicker date =
                new DatePicker(
                        old == null
                                ? LocalDate.now()
                                : old.getTransactionDate()
                );


        /*
         * PAYMENT METHOD
         */

        ComboBox<String> pm =
                new ComboBox<>();


        pm.getItems()
                .addAll(
                        "Cash",
                        "UPI",
                        "Debit Card",
                        "Credit Card",
                        "Bank Transfer",
                        "Other"
                );


        pm.setValue(
                old == null
                        ? "UPI"
                        : old.getPaymentMethod()
        );


        /*
         * EDIT MODE
         */

        if (old != null) {

            amount.setText(
                    String.valueOf(
                            old.getAmount()
                    )
            );


            desc.setText(
                    old.getDescription()
            );


            notes.setText(
                    old.getNotes() == null
                            ? ""
                            : old.getNotes()
            );


            cats.stream()
                    .filter(
                            x ->
                                    x.getId()
                                            ==
                                    old.getCategoryId()
                    )
                    .findFirst()
                    .ifPresent(
                            c::setValue
                    );
        }


        /*
         * FORM
         */

        g.addRow(
                0,
                new Label("Type"),
                t
        );


        g.addRow(
                1,
                new Label("Amount"),
                amount
        );


        g.addRow(
                2,
                new Label("Description"),
                desc
        );


        g.addRow(
                3,
                new Label("Category"),
                c
        );


        g.addRow(
                4,
                new Label("Date"),
                date
        );


        g.addRow(
                5,
                new Label("Payment"),
                pm
        );


        g.addRow(
                6,
                new Label("Notes"),
                notes
        );


        d.getDialogPane()
                .setContent(g);


        d.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );


        d.showAndWait()
                .ifPresent(
                        result -> {

                            if (result !=
                                    ButtonType.OK) {

                                return;
                            }


                            try {

                                /*
                                 * VALIDATION
                                 */

                                if (c.getValue() == null) {

                                    throw new IllegalArgumentException(
                                            "Choose a category."
                                    );
                                }


                                if (date.getValue() == null) {

                                    throw new IllegalArgumentException(
                                            "Choose a date."
                                    );
                                }


                                if (pm.getValue() == null) {

                                    throw new IllegalArgumentException(
                                            "Choose a payment method."
                                    );
                                }


                                double amountValue =
                                        Double.parseDouble(
                                                amount.getText()
                                        );


                                if (amountValue <= 0) {

                                    throw new IllegalArgumentException(
                                            "Amount must be greater than zero."
                                    );
                                }


                                /*
                                 * CREATE TRANSACTION OBJECT
                                 */

                                Transaction n =
                                        new Transaction(
                                                old == null
                                                        ? 0
                                                        : old.getId(),

                                                uid,

                                                c.getValue()
                                                        .getId(),

                                                amountValue,

                                                t.getValue(),

                                                desc.getText(),

                                                date.getValue(),

                                                pm.getValue(),

                                                notes.getText(),

                                                c.getValue()
                                                        .getName()
                                        );


                                /*
                                 * CREATE / UPDATE
                                 */

                                if (old == null) {

                                    service.create(n);

                                } else {

                                    service.update(n);
                                }


                                /*
                                 * REFRESH TABLE
                                 */

                                load();


                            } catch (Exception ex) {

                                UI.error(
                                        getScene().getWindow(),
                                        ex.getMessage()
                                );
                            }
                        }
                );
    }
}