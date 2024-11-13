package com.example.restaurantreservation.model;

import java.util.UUID;

public class Reservation {
    private final String id;
    private final User user;
    private final Table table;
    private final String startTime;
    private final String endTime;

    public Reservation(User user, Table table, String startTime, String endTime) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.table = table;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getId() { return id; }
    public User getUser() { return user; }  // Added this getter
    public Table getTable() { return table; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    public void confirmReservation() {
        System.out.println("Reservation confirmed for table " + table.getTableId() + "\n email confirmation sent, too");
    }

    public void cancelReservation() {
        table.cancelReservation();
        System.out.println("Reservation canceled.");
    }
}