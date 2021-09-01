package com.example.movietrailer.models.authentication;

public class User {

    private String uID;
    private String email;
    private String password;
    private String username;
    private String color;

    public User(String uID, String email, String password, String username, String color) {
        this.uID = uID;
        this.email = email;
        this.password = password;
        this.username = username;
        this.color = color;
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

    public String getColor() {
        return color;
    }
}
