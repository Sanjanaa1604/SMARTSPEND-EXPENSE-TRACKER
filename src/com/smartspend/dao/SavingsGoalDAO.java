package com.smartspend.dao;

import com.smartspend.model.SavingsGoal;
import com.smartspend.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class SavingsGoalDAO {

    public List<SavingsGoal> findAll(int uid) {

        List<SavingsGoal> a = new ArrayList<>();

        String q = """
                SELECT *
                FROM savings_goals
                WHERE user_id=?
                ORDER BY deadline
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setInt(1, uid);

            try (ResultSet r = p.executeQuery()) {

                while (r.next()) {

                    a.add(
                            new SavingsGoal(
                                    r.getInt("id"),
                                    uid,
                                    r.getString("name"),
                                    r.getDouble("target_amount"),
                                    r.getDouble("current_amount"),
                                    r.getDate("deadline").toLocalDate(),
                                    r.getString("description")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return a;
    }


    public void save(SavingsGoal g) {

        String q = """
                INSERT INTO savings_goals
                (user_id,name,target_amount,current_amount,deadline,description)
                VALUES(?,?,?,?,?,?)
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setInt(1, g.getUserId());
            p.setString(2, g.getName());
            p.setDouble(3, g.getTargetAmount());
            p.setDouble(4, g.getCurrentAmount());
            p.setDate(
                    5,
                    java.sql.Date.valueOf(g.getDeadline())
            );
            p.setString(6, g.getDescription());

            p.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateAmount(int id, int uid, double newAmount) {

        String q = """
                UPDATE savings_goals
                SET current_amount=?
                WHERE id=? AND user_id=?
                """;

        try (
                Connection c = DatabaseConnection.getConnection();
                PreparedStatement p = c.prepareStatement(q)
        ) {

            p.setDouble(1, newAmount);
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
                        "DELETE FROM savings_goals WHERE id=? AND user_id=?"
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