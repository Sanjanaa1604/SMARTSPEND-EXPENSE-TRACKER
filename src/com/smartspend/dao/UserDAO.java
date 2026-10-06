package com.smartspend.dao;
import com.smartspend.model.User; import com.smartspend.util.DatabaseConnection; import java.sql.*;
public class UserDAO {
 public User findByEmail(String email) throws SQLException {String q="SELECT * FROM users WHERE email=?"; try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,email);try(ResultSet r=p.executeQuery()){if(r.next())return map(r);}} return null;}
 public void updateProfile(int id,String name,String email) throws SQLException {String q="UPDATE users SET name=?, email=? WHERE id=?";try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,name);p.setString(2,email);p.setInt(3,id);p.executeUpdate();}}
 public int create(User u) throws SQLException {String q="INSERT INTO users(name,email,password_hash) VALUES(?,?,?)";try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q,Statement.RETURN_GENERATED_KEYS)){p.setString(1,u.getName());p.setString(2,u.getEmail());p.setString(3,u.getPasswordHash());p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){if(r.next())return r.getInt(1);}}return 0;}
 private User map(ResultSet r)throws SQLException{return new User(r.getInt("id"),r.getString("name"),r.getString("email"),r.getString("password_hash"),r.getTimestamp("created_at").toLocalDateTime());}
}
