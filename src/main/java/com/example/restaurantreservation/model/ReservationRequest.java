package com.example.restaurantreservation.model;

public class ReservationRequest {
    private String userEmail;
    private int tableId;
    private String startTime;
    private String endTime;

    // Default constructor for JSON deserialization
    public ReservationRequest() {}

    // Constructor with all fields
    public ReservationRequest(String userEmail, int tableId, String startTime, String endTime) {
        this.userEmail = userEmail;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}