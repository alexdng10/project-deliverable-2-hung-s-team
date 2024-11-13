package com.example.restaurantreservation.model;

public class ReservationRequest {
    private String userEmail; // Changed from User to String
    private Table table;
    private String startTime;
    private String endTime;

    // Getters and setters
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}