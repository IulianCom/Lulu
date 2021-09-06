package com.example.lulu.classes;

import com.example.lulu.utils.Utils;

import java.io.Serializable;

public class User implements Serializable {

    public static String REGULAR_USER_TYPE = "Regular user";
    public static String ARTIST_USER_TYPE = "Artist";

    private String uuid;
    private String email;
    private String password;
    private String name;
    private String userType;

//    public User(String email, String password, String userType) {
//        this.email = email;
//        this.password = password;
//        this.userType= userType;
//    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User(String email, String password, String name, String userType) {
        this.uuid = Utils.getCurrentUserUuid();
        this.email = email;
        this.password = password;
        this.name = name;
        this.userType= userType;
    }

    public User(){}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserType() { return userType; }

    public void setUserType(String userType) { this.userType = userType; }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", userType='" + userType + '\'' +
                '}';
    }
}


