package com.example.toolrental.models;

public class Booking {
    private int id;
    private int toolId;
    private int renterId;
    private String startDate;
    private String endDate;
    private String status; // "pending", "confirmed", "cancelled"
    private double totalPrice;

    public Booking(int id, int toolId, int renterId, String startDate, String endDate, String status, double totalPrice) {
        this.id = id;
        this.toolId = toolId;
        this.renterId = renterId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public int getId() { return id; }
    public int getToolId() { return toolId; }
    public int getRenterId() { return renterId; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
}
