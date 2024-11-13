package com.example.restaurantreservation.model;

public class TestReservationSystem {
    public static void main(String[] args) {
        // Create owner and franchise
        Owner owner = new Owner("John Doe", "john@example.com", "password", null);
        Franchise franchise = new Franchise("Test Location", owner);
        owner.setFranchise(franchise);

        // Add tables to franchise
        Table table1 = new Table(4);
        Table table2 = new Table(2);
        franchise.addTable(table1);
        franchise.addTable(table2);

        // Print available tables
        System.out.println("Available Tables:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table " + table.getTableId() + " (seats: " + table.getSeats() + ")");
        }

        // Create a user and make a reservation
        User user = new User("Jane Smith", "jane@example.com", "password", false);
        Reservation reservation = user.makeReservation(table1, "2024-01-01 18:00", "2024-01-01 20:00");

        // Check table availability after reservation
        System.out.println("\nAvailable Tables after reservation:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table " + table.getTableId() + " (seats: " + table.getSeats() + ")");
        }

        // Try to reserve an already reserved table
        User user2 = new User("Bob Wilson", "bob@example.com", "password", false);
        Reservation reservation2 = user2.makeReservation(table1, "2024-01-01 19:00", "2024-01-01 21:00");

        // Cancel reservation
        if (reservation != null) {
            user.cancelReservation(reservation);
        }

        // Check final table availability
        System.out.println("\nFinal Available Tables:");
        for (Table table : franchise.getTableAvailability()) {
            System.out.println("Table " + table.getTableId() + " (seats: " + table.getSeats() + ")");
        }
    }
}