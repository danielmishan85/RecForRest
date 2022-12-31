package com.example.recforrest.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    String fullName;
    String email;
    String password;
    List<Recommendation> recList;

    public User(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.recList = new ArrayList<>();
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<Recommendation> getRecList() {
        return recList;
    }
}
