package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final List<Reservation> reservations = new ArrayList<>();
    private final FranchiseController franchiseController;

    public ReservationController(FranchiseController franchiseController) {
        this.franchiseController = franchiseController;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        try {
            // Find user
            User user = User.findByEmail(request.getUserEmail());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Find table from all franchises
            Table table = null;
            List<Franchise> allFranchises = franchiseController.getAllFranchises().getBody(); // Fixed this line
            if (allFranchises != null) {
                for (Franchise franchise : allFranchises) {
                    Optional<Table> foundTable = franchise.getTables().stream()
                        .filter(t -> t.getTableId() == request.getTableId())
                        .findFirst();
                    if (foundTable.isPresent()) {
                        table = foundTable.get();
                        break;
                    }
                }
            }

            if (table == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            // Check if table is already reserved
            if (table.isReserved()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            // Create reservation
            Reservation reservation = user.makeReservation(
                table,
                request.getStartTime(),
                request.getEndTime()
            );

            if (reservation != null) {
                reservations.add(reservation);
                return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable String id) {
        try {
            Optional<Reservation> toDelete = reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst();

            if (toDelete.isPresent()) {
                Reservation reservation = toDelete.get();
                Table table = reservation.getTable();
                table.cancelReservation();
                reservations.remove(reservation);
                return ResponseEntity.ok("Reservation deleted successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reservation not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting reservation.");
        }
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable String id) {
        Optional<Reservation> reservation = reservations.stream()
            .filter(r -> r.getId().equals(id))
            .findFirst();

        return reservation
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable String email) {
        List<Reservation> userReservations = new ArrayList<>();
        for (Reservation reservation : reservations) {
            if (reservation.getUser().getEmail().equals(email)) {
                userReservations.add(reservation);
            }
        }
        return ResponseEntity.ok(userReservations);
    }
}