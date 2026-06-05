package com.example.toolrental.models;

public class User {
    private int id;
    private String email;
    private String password;
    private String role; // "renter" or "owner"
    private String fullName;

    public User(int id, String email, String password, String role, String fullName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
}
