package com.smartspend.analytics;
import java.util.*;
public class PredictionEngine {public double movingAverage(List<Double> monthly){if(monthly.isEmpty())return 0;return monthly.stream().mapToDouble(Double::doubleValue).average().orElse(0);} public double weightedAverage(List<Double> m){if(m.isEmpty())return 0;double s=0,w=0;for(int i=0;i<m.size();i++){double wt=i+1;s+=m.get(i)*wt;w+=wt;}return s/w;} public double projectMonthEnd(double spent,int elapsedDays,int totalDays){if(elapsedDays<=0)return spent;return spent/elapsedDays*totalDays;}}
