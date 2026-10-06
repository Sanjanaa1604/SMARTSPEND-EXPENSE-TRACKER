package com.smartspend.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SavingsGoal {
    private int id,userId; private String name,description; private double targetAmount,currentAmount; private LocalDate deadline;
    public SavingsGoal() {}
    public SavingsGoal(int id,int userId,String name,double target,double current,LocalDate deadline,String description){this.id=id;this.userId=userId;this.name=name;this.targetAmount=target;this.currentAmount=current;this.deadline=deadline;this.description=description;}
    public int getId(){return id;} public void setId(int v){id=v;} public int getUserId(){return userId;} public void setUserId(int v){userId=v;} public String getName(){return name;} public void setName(String v){name=v;} public double getTargetAmount(){return targetAmount;} public void setTargetAmount(double v){targetAmount=v;} public double getCurrentAmount(){return currentAmount;} public void setCurrentAmount(double v){currentAmount=v;} public LocalDate getDeadline(){return deadline;} public void setDeadline(LocalDate v){deadline=v;} public String getDescription(){return description;} public void setDescription(String v){description=v;}
    public double getProgress(){return targetAmount<=0?0:Math.min(100,currentAmount/targetAmount*100);}
    public double getRemaining(){return Math.max(0,targetAmount-currentAmount);}
    public long getDaysRemaining(){return Math.max(0,ChronoUnit.DAYS.between(LocalDate.now(),deadline));}
    public double getRequiredMonthlySaving(){long days=getDaysRemaining(); if(getRemaining()<=0)return 0; return days<=0?getRemaining():getRemaining()/(days/30.4375);}
}
