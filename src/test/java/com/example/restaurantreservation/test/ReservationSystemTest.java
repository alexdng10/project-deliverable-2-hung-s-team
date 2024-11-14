package com.example.restaurantreservation.test;

import com.example.restaurantreservation.model.*;
import com.example.restaurantreservation.controller.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationSystemTest {
    private FranchiseController franchiseController;
    private ReservationController reservationController;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @BeforeEach
    void setUp() {
        franchiseController = new FranchiseController();
        reservationController = new ReservationController(franchiseController);
    }

    private int getFirstAvailableTableId() {
        ResponseEntity<List<Franchise>> response = franchiseController.getAllFranchises();
        List<Franchise> franchises = response.getBody();
        assertNotNull(franchises);
        assertFalse(franchises.isEmpty());

        Franchise firstFranchise = franchises.get(0);
        List<Table> tables = firstFranchise.getTables();
        assertFalse(tables.isEmpty());
        
        Table firstTable = tables.get(0);
        System.out.println("Found table with ID: " + firstTable.getTableId());
        return firstTable.getTableId();
    }

    @Test
    void testDateTimeFormat() {
        String validDateTime = "2024-11-14T17:00:00";
        System.out.println("Testing datetime format: " + validDateTime);
        
        LocalDateTime parsed = LocalDateTime.parse(validDateTime, formatter);
        assertNotNull(parsed);
        assertEquals(17, parsed.getHour());
        assertEquals(0, parsed.getMinute());
        assertEquals(0, parsed.getSecond());
    }

    @Test
    void testBasicReservation() {
        int tableId = getFirstAvailableTableId();
        System.out.println("Using table ID: " + tableId);

        ReservationRequest request = new ReservationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setTableId(tableId);
        request.setStartTime("2024-11-14T17:00:00");

        System.out.println("Submitting reservation request:");
        System.out.println("Name: " + request.getName());
        System.out.println("Email: " + request.getEmail());
        System.out.println("TableId: " + request.getTableId());
        System.out.println("StartTime: " + request.getStartTime());

        ResponseEntity<?> response = reservationController.createReservation(request);
        System.out.println("Response Status: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        assertTrue(response.getStatusCode().is2xxSuccessful(), 
            "Reservation failed with response: " + response.getBody());
    }

    @Test
    void testInvalidTimeFormat() {
        int tableId = getFirstAvailableTableId();
        ReservationRequest request = new ReservationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setTableId(tableId);
        request.setStartTime("2024-11-14 17:00:00"); // Wrong format (space instead of T)

        ResponseEntity<?> response = reservationController.createReservation(request);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testTableAvailability() {
        int tableId = getFirstAvailableTableId();
        System.out.println("Testing table availability for table ID: " + tableId);

        // Make first reservation
        ReservationRequest request1 = new ReservationRequest();
        request1.setName("User 1");
        request1.setEmail("user1@example.com");
        request1.setTableId(tableId);
        request1.setStartTime("2024-11-14T17:00:00");

        ResponseEntity<?> response1 = reservationController.createReservation(request1);
        System.out.println("First reservation response: " + response1.getBody());
        assertTrue(response1.getStatusCode().is2xxSuccessful(),
            "First reservation failed with response: " + response1.getBody());

        // Try to reserve same table at same time
        ReservationRequest request2 = new ReservationRequest();
        request2.setName("User 2");
        request2.setEmail("user2@example.com");
        request2.setTableId(tableId);
        request2.setStartTime("2024-11-14T17:00:00");

        ResponseEntity<?> response2 = reservationController.createReservation(request2);
        System.out.println("Second reservation response: " + response2.getBody());
        assertEquals(409, response2.getStatusCodeValue(), 
            "Expected conflict status for duplicate reservation");
    }

    @Test
    void testBusinessHours() {
        int tableId = getFirstAvailableTableId();
        ReservationRequest request = new ReservationRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setTableId(tableId);
        request.setStartTime("2024-11-14T16:00:00"); // Before opening (5 PM)

        ResponseEntity<?> response = reservationController.createReservation(request);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Reservations are only available between"));
    }
}