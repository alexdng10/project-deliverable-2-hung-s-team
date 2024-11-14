package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {
    private final List<Reservation> reservations = new ArrayList<>();
    private final FranchiseController franchiseController;

    private static final LocalTime OPENING_TIME = LocalTime.of(17, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(23, 0);

    public ReservationController(FranchiseController franchiseController) {
        this.franchiseController = franchiseController;
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(request.getStartTime(), formatter);
            LocalTime startTime = startDateTime.toLocalTime();

            if (startTime.isBefore(OPENING_TIME) || startTime.isAfter(CLOSING_TIME)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("Reservations are only available between %s and %s",
                        OPENING_TIME.toString(),
                        CLOSING_TIME.toString()));
            }

            Table table = findTable(request.getTableId());
            if (table == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Table not found with ID: " + request.getTableId());
            }

            if (isTableReservedForTime(table, startDateTime)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Table is already reserved for this time slot");
            }

            User customer = new User(
                request.getName(),
                request.getEmail(),
                "password",
                false
            );

            LocalDateTime endDateTime = startDateTime.plusHours(2);
            Reservation reservation = customer.makeReservation(
                table,
                request.getStartTime(),
                endDateTime.format(formatter)
            );

            if (reservation != null) {
                reservations.add(reservation);
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createReservationResponse(reservation));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create reservation");
            }
        } catch (DateTimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid date/time format. Please use ISO format (YYYY-MM-DDTHH:mm:ss)");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while creating the reservation");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelReservation(@PathVariable String id) {
        try {
            Optional<Reservation> toCancel = reservations.stream()
                .filter(res -> res.getId().equals(id))
                .findFirst();

            if (toCancel.isPresent()) {
                Reservation reservation = toCancel.get();
                Table table = reservation.getTable();
                table.cancelReservation();
                reservations.remove(reservation);
                return ResponseEntity.ok("Reservation cancelled successfully");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Reservation not found");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error cancelling reservation");
        }
    }

    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> getTableReservations(@PathVariable int tableId) {
        try {
            List<ReservationResponse> tableReservations = reservations.stream()
                .filter(res -> res.getTable().getTableId() == tableId)
                .map(this::createReservationResponse)
                .collect(Collectors.toList());

            return ResponseEntity.ok(tableReservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching table reservations");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getCustomerReservations(@PathVariable String email) {
        try {
            List<ReservationResponse> customerReservations = reservations.stream()
                .filter(res -> res.getUser().getEmail().equals(email))
                .map(this::createReservationResponse)
                .collect(Collectors.toList());

            if (customerReservations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No reservations found for this email");
            }

            return ResponseEntity.ok(customerReservations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fetching customer reservations");
        }
    }

    // Ensure this method is properly declared
    private Table findTable(int tableId) {
        List<Franchise> allFranchises = franchiseController.getAllFranchises().getBody();
        if (allFranchises == null) return null;

        return allFranchises.stream()
            .flatMap(franchise -> franchise.getTables().stream())
            .filter(table -> table.getTableId() == tableId)
            .findFirst()
            .orElse(null);
    }

    private boolean isTableReservedForTime(Table table, LocalDateTime startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        return reservations.stream()
            .filter(res -> res.getTable().equals(table))
            .anyMatch(res -> {
                LocalDateTime resStart = LocalDateTime.parse(res.getStartTime(), formatter);
                LocalDateTime resEnd = LocalDateTime.parse(res.getEndTime(), formatter);
                return !startTime.isBefore(resStart) && startTime.isBefore(resEnd);
            });
    }

    private ReservationResponse createReservationResponse(Reservation reservation) {
        return new ReservationResponse(
            reservation.getId(),
            reservation.getUser().getName(),
            reservation.getUser().getEmail(),
            reservation.getTable().getTableId(),
            reservation.getStartTime(),
            reservation.getEndTime()
        );
    }

    // Add the static inner class here if not moved to its own file
    public static class ReservationResponse {
        private String id;
        private String customerName;
        private String email;
        private int tableId;
        private String startTime;
        private String endTime;

        public ReservationResponse(String id, String customerName, String email,
                                   int tableId, String startTime, String endTime) {
            this.id = id;
            this.customerName = customerName;
            this.email = email;
            this.tableId = tableId;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters
        public String getId() { return id; }
        public String getCustomerName() { return customerName; }
        public String getEmail() { return email; }
        public int getTableId() { return tableId; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
    }
}
