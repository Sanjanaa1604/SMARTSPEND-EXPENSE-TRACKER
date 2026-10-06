package com.smartspend.util;
import com.smartspend.model.User;
public final class Session { private static User user; private Session(){} public static User getUser(){return user;} public static void setUser(User u){user=u;} public static void clear(){user=null;} }
