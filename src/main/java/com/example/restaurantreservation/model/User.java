package com.example.restaurantreservation.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

public class User {
    protected String name;
    protected String email;
    protected String password;
    protected boolean isOwner;
    protected boolean reservedTable = false;

    // Path to CSV files - store in the resources folder
    private static final String USERS_CSV = "users.csv";
    private static final String RESERVATIONS_CSV = "reservations.csv";

    public User(String name, String email, String password, boolean isOwner) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOwner = isOwner;
        saveUserToCSV();
    }

    private void saveUserToCSV() {
        try {
            // Get the file from resources folder
            File file = new File(USERS_CSV);
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(String.format("%s,%s,%s,%b", name, email, password, isOwner));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reservation makeReservation(Table table, String startTime, String endTime) {
        if (reservedTable) {
            System.out.println("You have already reserved a table");
            return null;
        }

        if (!table.isReserved()) {
            Reservation reservation = new Reservation(this, table, startTime, endTime);
            table.reserve();
            saveReservationToCSV(reservation);
            reservedTable = true;
            return reservation;
        } else {
            System.out.println("Table is already reserved.");
            return null;
        }
    }

    private void saveReservationToCSV(Reservation reservation) {
        try {
            File file = new File(RESERVATIONS_CSV);
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(String.format("%s,%d,%s,%s", 
                    reservation.getId(), 
                    reservation.getTable().getTableId(),
                    reservation.getStartTime(),
                    reservation.getEndTime()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cancelReservation(Reservation reservation) {
        if (reservation != null && deleteReservation(reservation.getId())) {
            reservation.cancelReservation();
            reservedTable = false;
            System.out.println("Reservation canceled.");
        }
    }

    private boolean deleteReservation(String reservationId) {
        File inputFile = new File(RESERVATIONS_CSV);
        File tempFile = new File("temp_reservations.csv");
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                String[] data = currentLine.split(",");
                if (!data[0].equals(reservationId)) {
                    writer.write(currentLine);
                    writer.newLine();
                } else {
                    found = true;
                }
            }

            // Close resources before file operations
            reader.close();
            writer.close();

            // Replace old file with new file
            if (found) {
                if (inputFile.delete()) {
                    tempFile.renameTo(inputFile);
                }
            } else {
                tempFile.delete();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public boolean hasReservedTable() {
        return reservedTable;
    }

    // Static method to load all users from CSV
    public static List<User> loadAllUsers() {
        List<User> users = new ArrayList<>();
        File file = new File(USERS_CSV);
        
        if (!file.exists()) {
            return users;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    users.add(new User(
                        data[0],
                        data[1],
                        data[2],
                        Boolean.parseBoolean(data[3])
                    ));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    // Static method to find user by email
    public static User findByEmail(String email) {
        List<User> users = loadAllUsers();
        return users.stream()
            .filter(user -> user.getEmail().equals(email))
            .findFirst()
            .orElse(null);
    }
}