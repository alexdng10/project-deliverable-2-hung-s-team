package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.Reservation;
import com.example.restaurantreservation.model.ReservationRequest;
import com.example.restaurantreservation.model.User;
import com.example.restaurantreservation.model.Table;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest request) {
        User user = User.findByEmail(request.getUserEmail()); // Changed from getUser()
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Table table = request.getTable();
        if (table == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

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
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable String id) {
        Reservation toDelete = reservations.stream()
                .filter(reservation -> reservation.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (toDelete != null) {
            reservations.remove(toDelete);
            return ResponseEntity.ok("Reservation deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found.");
        }
    }
}