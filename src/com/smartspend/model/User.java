package com.smartspend.model;

import java.time.LocalDateTime;

public class User {
    private int id;
    private String name;
    private String email;
    private String passwordHash;
    private LocalDateTime createdAt;

    public User() {}
    public User(int id, String name, String email, String passwordHash, LocalDateTime createdAt) {
        this.id=id; this.name=name; this.email=email; this.passwordHash=passwordHash; this.createdAt=createdAt;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public String getName(){return name;} public void setName(String v){name=v;}
    public String getEmail(){return email;} public void setEmail(String v){email=v;}
    public String getPasswordHash(){return passwordHash;} public void setPasswordHash(String v){passwordHash=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
