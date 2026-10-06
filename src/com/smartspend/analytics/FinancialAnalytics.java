package com.smartspend.analytics;
import com.smartspend.model.*;import java.time.*;import java.util.*;
public class FinancialAnalytics {
 public double total(List<Transaction> t,String type){return t.stream().filter(x->x.getType().equals(type)).mapToDouble(Transaction::getAmount).sum();}
 public double savingsRate(double income,double expense){return income<=0?0:Math.max(0,(income-expense)/income*100);}
 public Map<String,Double> byCategory(List<Transaction> t){Map<String,Double> m=new LinkedHashMap<>();for(Transaction x:t)if("EXPENSE".equals(x.getType()))m.merge(x.getCategoryName(),x.getAmount(),Double::sum);return m;}
 public double averageDaily(List<Transaction> t){if(t.isEmpty())return 0;LocalDate min=t.stream().map(Transaction::getTransactionDate).min(LocalDate::compareTo).orElse(LocalDate.now());long days=Math.max(1,java.time.temporal.ChronoUnit.DAYS.between(min,LocalDate.now())+1);return total(t,"EXPENSE")/days;}
 public double weekendPercentage(List<Transaction> t){double all=total(t,"EXPENSE"),week=t.stream().filter(x->x.getType().equals("EXPENSE")&&(x.getTransactionDate().getDayOfWeek()==DayOfWeek.SATURDAY||x.getTransactionDate().getDayOfWeek()==DayOfWeek.SUNDAY)).mapToDouble(Transaction::getAmount).sum();return all==0?0:week/all*100;}
 public double healthScore(double savingsRate,double budgetUse,double consistency,double goalProgress,double recurringBurden){double savings=Math.min(100,Math.max(0,savingsRate));double budget=Math.max(0,100-Math.max(0,budgetUse-70)*2);double goal=Math.min(100,Math.max(0,goalProgress));double recurring=Math.max(0,100-recurringBurden);return Math.round((savings*.30+budget*.25+consistency*.15+goal*.20+recurring*.10));}
 public Set<String> uniqueCategories(List<Transaction> t){Set<String>s=new LinkedHashSet<>();for(Transaction x:t)if(x.getCategoryName()!=null)s.add(x.getCategoryName());return s;}
 public String highestCategory(Map<String,Double> m){return m.entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("None");}
}
