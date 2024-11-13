package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.Reservation;
import com.example.restaurantreservation.model.ReservationRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();

    // POST /api/reservations
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
            reservationRequest.getUser(),
            reservationRequest.getTable(),
            reservationRequest.getStartTime(),
            reservationRequest.getEndTime()
        );
        reservations.add(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservation);
    }

    // DELETE /api/reservations/{id}
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
