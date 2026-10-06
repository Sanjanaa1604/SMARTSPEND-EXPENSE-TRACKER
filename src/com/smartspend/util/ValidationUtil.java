package com.smartspend.util;
import java.util.regex.Pattern;
public final class ValidationUtil { private ValidationUtil(){} private static final Pattern EMAIL=Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
 public static boolean email(String s){return s!=null&&EMAIL.matcher(s.trim()).matches();}
 public static boolean password(String s){return s!=null&&s.length()>=8;}
 public static boolean positive(double n){return Double.isFinite(n)&&n>0;}
}
