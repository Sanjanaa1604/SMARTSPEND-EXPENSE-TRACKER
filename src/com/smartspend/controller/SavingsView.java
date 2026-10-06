package com.smartspend.controller;

import com.smartspend.dao.SavingsGoalDAO;
import com.smartspend.model.SavingsGoal;
import com.smartspend.util.*;

import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;

public class SavingsView extends VBox {

    private final SavingsGoalDAO dao = new SavingsGoalDAO();

    private final int uid = Session.getUser().getId();

    private final VBox list = new VBox(12);


    public SavingsView() {

        setPadding(new Insets(5, 30, 30, 30));
        setSpacing(15);

        Button add = UI.button("+ New Goal");

        add.setOnAction(e -> dialog());

        getChildren().addAll(
                UI.title("Savings Goals"),
                UI.subtitle(
                        "Turn future purchases into measurable targets."
                ),
                add,
                list
        );

        load();
    }


    private void load() {

        list.getChildren().clear();

        for (SavingsGoal g : dao.findAll(uid)) {

            ProgressBar p =
                    new ProgressBar(
                            g.getProgress() / 100
                    );

            p.setMaxWidth(Double.MAX_VALUE);


            Label title =
                    new Label(
                            g.getName()
                                    + "   "
                                    + UI.money(g.getCurrentAmount())
                                    + " / "
                                    + UI.money(g.getTargetAmount())
                                    + "   "
                                    + String.format(
                                            "%.0f%%",
                                            g.getProgress()
                                    )
                    );


            Label details =
                    new Label(
                            "Remaining "
                                    + UI.money(g.getRemaining())
                                    + " • "
                                    + g.getDaysRemaining()
                                    + " days • Required/month "
                                    + UI.money(
                                            g.getRequiredMonthlySaving()
                                    )
                    );


            HBox actions = new HBox(8);


            Button addMoney =
                    UI.button("+ Add Money");

            addMoney.setOnAction(
                    e -> addMoneyDialog(g)
            );


            Button delete =
                    UI.ghost("Delete");

            delete.getStyleClass()
                    .add("danger-button");

            delete.setOnAction(
                    e -> {

                        dao.delete(g.getId(), uid);

                        load();
                    }
            );


            actions.getChildren().addAll(
                    addMoney,
                    delete
            );


            VBox card =
                    UI.card(
                            title,
                            p,
                            details,
                            actions
                    );


            if (g.getRemaining() <= 0) {

                Label completed =
                        new Label("✓ Goal completed 🎉");

                completed.getStyleClass()
                        .add("income-amount");

                card.getChildren()
                        .add(completed);
            }


            list.getChildren().add(card);
        }
    }


    private void addMoneyDialog(SavingsGoal goal) {

        Dialog<ButtonType> d =
                new Dialog<>();

        d.setTitle("Add Money");


        GridPane grid =
                new GridPane();

        grid.setVgap(10);
        grid.setHgap(10);


        TextField amount =
                UI.input("Amount to add");


        Label current =
                new Label(
                        "Currently saved: "
                                + UI.money(
                                        goal.getCurrentAmount()
                                )
                );


        Label target =
                new Label(
                        "Target: "
                                + UI.money(
                                        goal.getTargetAmount()
                                )
                );


        grid.addRow(
                0,
                new Label("Current"),
                current
        );

        grid.addRow(
                1,
                new Label("Target"),
                target
        );

        grid.addRow(
                2,
                new Label("Add"),
                amount
        );


        d.getDialogPane()
                .setContent(grid);

        d.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );


        d.showAndWait()
                .ifPresent(result -> {

                    if (result != ButtonType.OK) {
                        return;
                    }

                    try {

                        double add =
                                Double.parseDouble(
                                        amount.getText()
                                );


                        if (add <= 0) {

                            throw new IllegalArgumentException(
                                    "Amount must be greater than zero."
                            );
                        }


                        double newAmount =
                                goal.getCurrentAmount()
                                        + add;


                        if (newAmount >
                                goal.getTargetAmount()) {

                            throw new IllegalArgumentException(
                                    "Amount exceeds the remaining goal amount."
                            );
                        }


                        dao.updateAmount(
                                goal.getId(),
                                uid,
                                newAmount
                        );


                        load();

                    } catch (Exception e) {

                        UI.error(
                                getScene().getWindow(),
                                e.getMessage()
                        );
                    }
                });
    }


    private void dialog() {

        Dialog<ButtonType> d =
                new Dialog<>();

        GridPane g =
                new GridPane();

        g.setVgap(9);
        g.setHgap(10);


        TextField n =
                UI.input("Goal name");

        TextField target =
                UI.input("Target amount");

        TextField current =
                UI.input("Current amount");

        TextField desc =
                UI.input("Description");


        DatePicker date =
                new DatePicker(
                        LocalDate.now()
                                .plusMonths(6)
                );


        g.addRow(
                0,
                new Label("Name"),
                n
        );

        g.addRow(
                1,
                new Label("Target"),
                target
        );

        g.addRow(
                2,
                new Label("Saved"),
                current
        );

        g.addRow(
                3,
                new Label("Deadline"),
                date
        );

        g.addRow(
                4,
                new Label("Description"),
                desc
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

                            double targetAmount =
                                    Double.parseDouble(
                                            target.getText()
                                    );

                            double currentAmount =
                                    Double.parseDouble(
                                            current.getText()
                                    );


                            if (targetAmount <= 0) {

                                throw new IllegalArgumentException(
                                        "Target must be greater than zero."
                                );
                            }


                            if (currentAmount < 0) {

                                throw new IllegalArgumentException(
                                        "Saved amount cannot be negative."
                                );
                            }


                            if (currentAmount >
                                    targetAmount) {

                                throw new IllegalArgumentException(
                                        "Saved amount cannot exceed target."
                                );
                            }


                            dao.save(
                                    new SavingsGoal(
                                            0,
                                            uid,
                                            n.getText(),
                                            targetAmount,
                                            currentAmount,
                                            date.getValue(),
                                            desc.getText()
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