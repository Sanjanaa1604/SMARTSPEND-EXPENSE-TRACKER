package com.smartspend.dao;

import com.smartspend.model.RecurringExpense;
import com.smartspend.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class RecurringExpenseDAO {

    public List<RecurringExpense> findAll(int uid) {

        List<RecurringExpense> a = new ArrayList<>();

        String q = """
                SELECT r.*, c.name category_name
                FROM recurring_expenses r
                JOIN categories c ON c.id=r.category_id
                WHERE r.user_id=?
                ORDER BY r.next_payment_date
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setInt(1, uid);

            try (ResultSet r = p.executeQuery()) {

                while (r.next()) {

                    a.add(
                            new RecurringExpense(
                                    r.getInt("id"),
                                    uid,
                                    r.getString("name"),
                                    r.getDouble("amount"),
                                    r.getInt("category_id"),
                                    r.getString("frequency"),
                                    r.getDate(
                                            "next_payment_date"
                                    ).toLocalDate(),
                                    r.getString("category_name")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return a;
    }


    public void save(RecurringExpense x) {

        String q = """
                INSERT INTO recurring_expenses
                (user_id,name,amount,category_id,frequency,next_payment_date)
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setInt(1, x.getUserId());
            p.setString(2, x.getName());
            p.setDouble(3, x.getAmount());
            p.setInt(4, x.getCategoryId());
            p.setString(5, x.getFrequency());
            p.setDate(
                    6,
                    java.sql.Date.valueOf(
                            x.getNextPaymentDate()
                    )
            );

            p.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateNextPayment(
            int id,
            int uid,
            LocalDate nextDate
    ) {

        String q = """
                UPDATE recurring_expenses
                SET next_payment_date=?
                WHERE id=? AND user_id=?
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setDate(
                    1,
                    java.sql.Date.valueOf(nextDate)
            );

            p.setInt(2, id);
            p.setInt(3, uid);

            p.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void delete(int id, int uid) {

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(
                        "DELETE FROM recurring_expenses WHERE id=? AND user_id=?"
                )
        ) {

            p.setInt(1, id);
            p.setInt(2, uid);

            p.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}