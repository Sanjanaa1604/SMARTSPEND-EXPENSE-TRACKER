package com.smartspend.model;

public class Category {
    private int id; private String name; private String type;
    public Category() {}
    public Category(int id,String name,String type){this.id=id;this.name=name;this.type=type;}
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getType(){return type;} public void setType(String v){type=v;}
    @Override public String toString(){return name;}
}
