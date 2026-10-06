package com.smartspend.model;

import java.time.LocalDate;

public class Transaction {
    private int id,userId,categoryId;
    private double amount;
    private String type,description,paymentMethod,notes;
    private LocalDate transactionDate;
    private String categoryName;

    public Transaction() {}
    public Transaction(int id,int userId,int categoryId,double amount,String type,String description,LocalDate date,String paymentMethod,String notes,String categoryName){
        this.id=id;this.userId=userId;this.categoryId=categoryId;this.amount=amount;this.type=type;this.description=description;this.transactionDate=date;this.paymentMethod=paymentMethod;this.notes=notes;this.categoryName=categoryName;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getUserId(){return userId;} public void setUserId(int v){userId=v;}
    public int getCategoryId(){return categoryId;} public void setCategoryId(int v){categoryId=v;}
    public double getAmount(){return amount;} public void setAmount(double v){amount=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public LocalDate getTransactionDate(){return transactionDate;} public void setTransactionDate(LocalDate v){transactionDate=v;}
    public String getPaymentMethod(){return paymentMethod;} public void setPaymentMethod(String v){paymentMethod=v;}
    public String getNotes(){return notes;} public void setNotes(String v){notes=v;}
    public String getCategoryName(){return categoryName;} public void setCategoryName(String v){categoryName=v;}
}
