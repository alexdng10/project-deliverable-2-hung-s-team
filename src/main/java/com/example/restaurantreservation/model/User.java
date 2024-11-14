package com.example.restaurantreservation.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Paths;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

public class User {
    protected String name;
    protected String email;
    protected String password;
    protected boolean isOwner;
    protected boolean reservedTable = false;
    private boolean shouldSave = true;  // New flag to control saving

    // Path to CSV files in resources folder
    private static final String USERS_CSV = "src/main/resources/users.csv";
    private static final String RESERVATIONS_CSV = "src/main/resources/reservations.csv";

    public User(String name, String email, String password, boolean isOwner) {
        this(name, email, password, isOwner, true);
    }

    // Private constructor with save control
    private User(String name, String email, String password, boolean isOwner, boolean shouldSave) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isOwner = isOwner;
        this.shouldSave = shouldSave;
        
        if (shouldSave && findByEmail(email) == null) {
            saveUserToCSV();
        }
    }

    private void saveUserToCSV() {
        File file = new File(USERS_CSV);
        file.getParentFile().mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Check if file is empty to handle CSV header
            if (file.length() == 0) {
                writer.write("name,email,password,isOwner");
                writer.newLine();
            }
            writer.write(String.format("%s,%s,%s,%b", name, email, password, isOwner));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Reservation makeReservation(Table table, String startTime, String endTime) {
        if (reservedTable) {
            throw new IllegalStateException("User already has an active reservation");
        }

        if (!table.isReserved()) {
            Reservation reservation = new Reservation(this, table, startTime, endTime);
            table.reserve();
            saveReservationToCSV(reservation);
            reservedTable = true;
            return reservation;
        } else {
            throw new IllegalStateException("Table is already reserved");
        }
    }

    private void saveReservationToCSV(Reservation reservation) {
        File file = new File(RESERVATIONS_CSV);
        file.getParentFile().mkdirs();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            // Add header if file is empty
            if (file.length() == 0) {
                writer.write("reservationId,tableId,userEmail,startTime,endTime");
                writer.newLine();
            }
            writer.write(String.format("%s,%d,%s,%s,%s", 
                reservation.getId(), 
                reservation.getTable().getTableId(),
                this.email,
                reservation.getStartTime(),
                reservation.getEndTime()
            ));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                // Skip header line
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] data = line.split(",");
                if (data.length == 4) {
                    // Create user without saving to CSV
                    users.add(new User(
                        data[0],
                        data[1],
                        data[2],
                        Boolean.parseBoolean(data[3]),
                        false  // Don't save when loading
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

    // Getters
    public String getName() { return name; }
    public String getEmail() { return email; }
    public boolean isOwner() { return isOwner; }
    public boolean hasReservedTable() { return reservedTable; }
}