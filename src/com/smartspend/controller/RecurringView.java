package com.smartspend.controller;

import com.smartspend.dao.*;
import com.smartspend.model.*;
import com.smartspend.service.TransactionService;
import com.smartspend.util.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.*;
import java.time.temporal.ChronoUnit;

public class RecurringView extends VBox {

    private final RecurringExpenseDAO dao =
            new RecurringExpenseDAO();

    private final CategoryDAO cd =
            new CategoryDAO();

    private final TransactionService transactionService =
            new TransactionService();

    private final int uid =
            Session.getUser().getId();

    private final VBox list =
            new VBox(10);


    public RecurringView() {

        setPadding(
                new Insets(5, 30, 30, 30)
        );

        setSpacing(15);


        Button add =
                UI.button("+ Add Recurring Payment");

        add.setOnAction(
                e -> dialog()
        );


        getChildren().addAll(
                UI.title("Recurring Payments"),
                UI.subtitle(
                        "Never lose track of subscriptions and scheduled bills."
                ),
                add,
                list
        );


        load();
    }


    private void load() {

        list.getChildren().clear();


        for (RecurringExpense x :
                dao.findAll(uid)) {

            long days =
                    Math.max(
                            0,
                            ChronoUnit.DAYS.between(
                                    LocalDate.now(),
                                    x.getNextPaymentDate()
                            )
                    );


            Label title =
                    new Label(
                            x.getName()
                                    + "  •  "
                                    + UI.money(
                                            x.getAmount()
                                    )
                    );


            Label details =
                    new Label(
                            x.getCategoryName()
                                    + "  •  "
                                    + x.getFrequency()
                                    + "  •  Due in "
                                    + days
                                    + " days"
                    );


            HBox actions =
                    new HBox(8);


            Button paid =
                    UI.button("✓ Mark as Paid");


            paid.setOnAction(
                    e -> markAsPaid(x)
            );


            Button del =
                    UI.ghost("Delete");

            del.getStyleClass()
                    .add("danger-button");

            del.setOnAction(
                    e -> {

                        dao.delete(
                                x.getId(),
                                uid
                        );

                        load();
                    }
            );


            actions.getChildren().addAll(
                    paid,
                    del
            );


            list.getChildren().add(
                    UI.card(
                            title,
                            details,
                            actions
                    )
            );
        }
    }


    private void markAsPaid(
            RecurringExpense x
    ) {

        try {

            LocalDate paymentDate =
                    LocalDate.now();


            Transaction transaction =
                    new Transaction(
                            0,
                            uid,
                            x.getCategoryId(),
                            x.getAmount(),
                            "EXPENSE",
                            x.getName(),
                            paymentDate,
                            "Other",
                            "Recurring payment",
                            x.getCategoryName()
                    );


            transactionService.create(
                    transaction
            );


            LocalDate nextDate =
                    calculateNextDate(
                            x.getNextPaymentDate(),
                            x.getFrequency()
                    );


            while (!nextDate.isAfter(paymentDate)) {

                nextDate =
                        calculateNextDate(
                                nextDate,
                                x.getFrequency()
                        );
            }


            dao.updateNextPayment(
                    x.getId(),
                    uid,
                    nextDate
            );


            load();


        } catch (Exception e) {

            UI.error(
                    getScene().getWindow(),
                    e.getMessage()
            );
        }
    }


    private LocalDate calculateNextDate(
            LocalDate date,
            String frequency
    ) {

        return switch (
                frequency.toLowerCase()
        ) {

            case "weekly" ->
                    date.plusWeeks(1);

            case "yearly" ->
                    date.plusYears(1);

            default ->
                    date.plusMonths(1);
        };
    }


    private void dialog() {

        Dialog<ButtonType> d =
                new Dialog<>();

        GridPane g =
                new GridPane();

        g.setVgap(9);
        g.setHgap(10);


        TextField n =
                UI.input("Name");

        TextField a =
                UI.input("Amount");


        ComboBox<Category> c =
                new ComboBox<>();

        c.getItems()
                .addAll(
                        cd.findAll()
                );


        ComboBox<String> f =
                new ComboBox<>();

        f.getItems().addAll(
                "Weekly",
                "Monthly",
                "Yearly"
        );

        f.setValue("Monthly");


        DatePicker date =
                new DatePicker(
                        LocalDate.now()
                                .plusDays(7)
                );


        g.addRow(
                0,
                new Label("Name"),
                n
        );

        g.addRow(
                1,
                new Label("Amount"),
                a
        );

        g.addRow(
                2,
                new Label("Category"),
                c
        );

        g.addRow(
                3,
                new Label("Frequency"),
                f
        );

        g.addRow(
                4,
                new Label("Next date"),
                date
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
                .ifPresent(x -> {

                    if (x == ButtonType.OK) {

                        try {

                            if (c.getValue() == null) {

                                throw new IllegalArgumentException(
                                        "Choose a category."
                                );
                            }


                            double amount =
                                    Double.parseDouble(
                                            a.getText()
                                    );


                            if (amount <= 0) {

                                throw new IllegalArgumentException(
                                        "Amount must be greater than zero."
                                );
                            }


                            dao.save(
                                    new RecurringExpense(
                                            0,
                                            uid,
                                            n.getText(),
                                            amount,
                                            c.getValue().getId(),
                                            f.getValue(),
                                            date.getValue(),
                                            c.getValue().getName()
                                    )
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
}