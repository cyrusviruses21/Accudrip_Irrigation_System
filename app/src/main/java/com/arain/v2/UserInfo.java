package com.arain.v2;

public class UserInfo {

    public String fullName, phoneNumber, email;

    public UserInfo() {
        // Default constructor required for Firebase
    }

    public UserInfo(String fullName, String phoneNumber, String email)
    {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

}


