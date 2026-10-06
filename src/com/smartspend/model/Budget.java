package com.smartspend.model;

public class Budget {
    private int id,userId,categoryId,month,year; private double amount,spent;
    private String categoryName;
    public Budget() {}
    public Budget(int id,int userId,int categoryId,double amount,int month,int year,double spent,String categoryName){this.id=id;this.userId=userId;this.categoryId=categoryId;this.amount=amount;this.month=month;this.year=year;this.spent=spent;this.categoryName=categoryName;}
    public int getId(){return id;} public void setId(int v){id=v;} public int getUserId(){return userId;} public void setUserId(int v){userId=v;} public int getCategoryId(){return categoryId;} public void setCategoryId(int v){categoryId=v;} public double getAmount(){return amount;} public void setAmount(double v){amount=v;} public int getMonth(){return month;} public void setMonth(int v){month=v;} public int getYear(){return year;} public void setYear(int v){year=v;} public double getSpent(){return spent;} public void setSpent(double v){spent=v;} public String getCategoryName(){return categoryName;} public void setCategoryName(String v){categoryName=v;}
    public double getPercentage(){return amount<=0?0:(spent/amount)*100.0;}
}
