package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.Franchise;
import com.example.restaurantreservation.model.Table;
import com.example.restaurantreservation.model.Owner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {  
    private final List<Franchise> franchises = new ArrayList<>();

    public FranchiseController() {
        // Initialize with index-based IDs
        Owner defaultOwner = new Owner("Default Owner", "owner@example.com", "password", null);

        // Downtown Restaurant (ID: 0)
        Franchise downtown = new Franchise("Downtown Location", defaultOwner);
        downtown.addTable(new Table(4));
        downtown.addTable(new Table(2));
        downtown.addTable(new Table(6));
        franchises.add(downtown);
        defaultOwner.setFranchise(downtown);

        // Uptown Restaurant (ID: 1)
        Owner uptownOwner = new Owner("Uptown Owner", "uptown@example.com", "password", null);
        Franchise uptown = new Franchise("Uptown Location", uptownOwner);
        uptown.addTable(new Table(4));
        uptown.addTable(new Table(2));
        franchises.add(uptown);
        uptownOwner.setFranchise(uptown);

        // Midtown Restaurant (ID: 2)
        Owner midtownOwner = new Owner("Midtown Owner", "midtown@example.com", "password", null);
        Franchise midtown = new Franchise("Midtown Location", midtownOwner);
        midtown.addTable(new Table(4));
        midtown.addTable(new Table(2));
        franchises.add(midtown);
        midtownOwner.setFranchise(midtown);
    }

    @GetMapping
    public ResponseEntity<List<Franchise>> getAllFranchises() {
        return ResponseEntity.ok(franchises);
    }

    @GetMapping("/{franchiseId}/tables")
    public ResponseEntity<List<Table>> getTablesByFranchise(@PathVariable String franchiseId) {
        try {
            int index = Integer.parseInt(franchiseId);
            if (index >= 0 && index < franchises.size()) {
                return ResponseEntity.ok(franchises.get(index).getTableAvailability());
            }
            return ResponseEntity.notFound().build();
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}