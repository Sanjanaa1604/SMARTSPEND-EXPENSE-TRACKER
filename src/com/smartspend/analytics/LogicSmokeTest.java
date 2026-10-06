package com.smartspend.analytics;
import java.util.*;
public class LogicSmokeTest {public static void main(String[]args){PredictionEngine p=new PredictionEngine();if(Math.round(p.movingAverage(List.of(100.0,200.0,300.0)))!=200)throw new AssertionError();if(Math.round(p.weightedAverage(List.of(100.0,200.0,300.0)))!=233)throw new AssertionError();System.out.println("SmartSpend logic smoke tests passed.");}}
