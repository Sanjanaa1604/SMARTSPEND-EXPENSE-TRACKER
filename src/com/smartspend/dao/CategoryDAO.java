package com.smartspend.dao;
import com.smartspend.model.Category; import com.smartspend.util.DatabaseConnection; import java.sql.*; import java.util.*;
public class CategoryDAO { public List<Category> findAll(){List<Category> a=new ArrayList<>();String q="SELECT * FROM categories ORDER BY name";try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){while(r.next())a.add(new Category(r.getInt("id"),r.getString("name"),r.getString("type")));}catch(SQLException e){throw new RuntimeException(e);}return a;} }
