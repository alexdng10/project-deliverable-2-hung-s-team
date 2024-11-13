package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.Franchise;
import com.example.restaurantreservation.model.Table;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {

    private final List<Franchise> franchises = new ArrayList<>();

    // GET /api/franchises
    @GetMapping
    public List<Franchise> getAllFranchises() {
        return franchises;
    }

    // GET /api/franchises/{franchiseId}/tables
    @GetMapping("/{franchiseId}/tables")
    public List<Table> getTablesByFranchise(@PathVariable int franchiseId) {
        Franchise franchise = franchises.get(franchiseId); // Retrieve franchise by ID (simplified)
        return franchise.getTableAvailability();
    }
}
