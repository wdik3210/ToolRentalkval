package com.example.toolrental.models;

public class Tool {
    private int id;
    private String name;
    private String description;
    private double pricePerDay;
    private String imageUri;
    private int ownerId;

    public Tool(int id, String name, String description, double pricePerDay, String imageUri, int ownerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pricePerDay = pricePerDay;
        this.imageUri = imageUri;
        this.ownerId = ownerId;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPricePerDay() { return pricePerDay; }
    public String getImageUri() { return imageUri; }
    public int getOwnerId() { return ownerId; }
}

