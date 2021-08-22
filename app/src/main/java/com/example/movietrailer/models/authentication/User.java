package com.example.movietrailer.models.authentication;

public class User {

    private String uID;
    private String email;
    private String password;
    private String username;

    public User(String uID, String email, String password, String username) {
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public String getuID() {
        return uID;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
