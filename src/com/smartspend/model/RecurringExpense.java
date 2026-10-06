package com.smartspend.model;
import java.time.LocalDate;
public class RecurringExpense {
 private int id,userId,categoryId; private String name,frequency,categoryName; private double amount; private LocalDate nextPaymentDate;
 public RecurringExpense(){}
 public RecurringExpense(int id,int userId,String name,double amount,int categoryId,String frequency,LocalDate date,String categoryName){this.id=id;this.userId=userId;this.name=name;this.amount=amount;this.categoryId=categoryId;this.frequency=frequency;this.nextPaymentDate=date;this.categoryName=categoryName;}
 public int getId(){return id;} public void setId(int v){id=v;} public int getUserId(){return userId;} public void setUserId(int v){userId=v;} public String getName(){return name;} public void setName(String v){name=v;} public double getAmount(){return amount;} public void setAmount(double v){amount=v;} public int getCategoryId(){return categoryId;} public void setCategoryId(int v){categoryId=v;} public String getFrequency(){return frequency;} public void setFrequency(String v){frequency=v;} public LocalDate getNextPaymentDate(){return nextPaymentDate;} public void setNextPaymentDate(LocalDate v){nextPaymentDate=v;} public String getCategoryName(){return categoryName;} public void setCategoryName(String v){categoryName=v;}
}
