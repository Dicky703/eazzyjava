package com.dixon.eazzytravel.activity;

public class User {
    private int id;
    private String first_name, last_name, username, email;

    public User(int id, String first_name, String last_name, String username, String email){
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.username = username;
        this.email = email;
    }
    public int getId(){
        return id;
    }
    public String getFirstName(){
        return first_name;
    }
    public String getLastName(){
        return last_name;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
}
