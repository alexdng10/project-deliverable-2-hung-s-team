package com.example.restaurantreservation.model;

public class ReservationRequest {
    private String name;
    private String email;
    private int tableId;
    private String startTime;
    private String endTime;

    // Default constructor
    public ReservationRequest() {}

    // Constructor with all fields
    public ReservationRequest(String name, String email, int tableId, 
                            String startTime, String endTime) {
        this.name = name;
        this.email = email;
        this.tableId = tableId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and setters
    public String getName() { 
        return name; 
    }
    
    public void setName(String name) { 
        this.name = name; 
    }

    public String getEmail() { 
        return email; 
    }
    
    public void setEmail(String email) { 
        this.email = email; 
    }

    public int getTableId() { 
        return tableId; 
    }
    
    public void setTableId(int tableId) { 
        this.tableId = tableId; 
    }

    public String getStartTime() { 
        return startTime; 
    }
    
    public void setStartTime(String startTime) { 
        this.startTime = startTime; 
    }

    public String getEndTime() { 
        return endTime; 
    }
    
    public void setEndTime(String endTime) { 
        this.endTime = endTime; 
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
            "name='" + name + '\'' +
            ", email='" + email + '\'' +
            ", tableId=" + tableId +
            ", startTime='" + startTime + '\'' +
            ", endTime='" + endTime + '\'' +
            '}';
    }
}