package com.kupang.androidbpbd.bpbdkupang.model;

public class User {
    private int userId;
    private String name;
    private String telephoneNumber;
    private String email;

    public User(){
    }

    public User(String name, String telephoneNumber, String email){
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public User(int userId, String name, String telephoneNumber, String email){
        this.userId = userId;
        this.name = name;
        this.telephoneNumber = telephoneNumber;
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
