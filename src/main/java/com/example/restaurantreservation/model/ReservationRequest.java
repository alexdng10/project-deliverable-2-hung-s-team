package com.example.restaurantreservation.model;

public class ReservationRequest {
    private User user;
    private Table table;
    private String startTime;
    private String endTime;

    // Constructors, getters, and setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Table getTable() { return table; }
    public void setTable(Table table) { this.table = table; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
