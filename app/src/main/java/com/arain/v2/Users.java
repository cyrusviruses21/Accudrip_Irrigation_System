package com.arain.v2;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Users implements Serializable {

    @Exclude
    private String key;
    private String fullName;
    private String email;
    private  String phoneNumber;

    public Users(){}


    public Users(String fullName, String email, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {return phoneNumber;}

    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
