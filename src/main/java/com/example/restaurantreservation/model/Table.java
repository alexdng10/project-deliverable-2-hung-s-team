// src/main/java/com/example/restaurantreservation/model/Table.java
package com.example.restaurantreservation.model;

public class Table {
    private static int idCounter = 1;
    private int tableId;
    private int seats;
    private boolean isReserved;

    public Table(int seats) {
        this.tableId = idCounter++;
        this.seats = seats;
        this.isReserved = false;
    }

    public boolean isReserved() {
        return isReserved;
    }

    public void reserve() {
        this.isReserved = true;
    }

    public void cancelReservation() {
        this.isReserved = false;
    }

    public int getTableId() {
        return tableId;
    }

    public int getSeats() {
        return seats;
    }
}