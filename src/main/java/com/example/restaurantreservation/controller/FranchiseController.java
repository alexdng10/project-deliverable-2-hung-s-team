package com.example.restaurantreservation.controller;

import com.example.restaurantreservation.model.Franchise;
import com.example.restaurantreservation.model.Table;
import com.example.restaurantreservation.model.Owner;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/franchises")
public class FranchiseController {
    private final List<Franchise> franchises = new ArrayList<>();

    public FranchiseController() {
        // Create a default owner for initialization
        Owner defaultOwner = new Owner("Default Owner", "owner@example.com", "password", null);

        // Initialize Downtown Restaurant
        Franchise downtown = new Franchise("Downtown Location", defaultOwner);
        downtown.addTable(new Table(4));
        downtown.addTable(new Table(2));
        downtown.addTable(new Table(6));
        franchises.add(downtown);
        defaultOwner.setFranchise(downtown); // Set franchise after creation

        // Initialize Uptown Restaurant
        Owner uptownOwner = new Owner("Uptown Owner", "uptown@example.com", "password", null);
        Franchise uptown = new Franchise("Uptown Location", uptownOwner);
        uptown.addTable(new Table(4));
        uptown.addTable(new Table(2));
        franchises.add(uptown);
        uptownOwner.setFranchise(uptown);

        // Initialize Midtown Restaurant
        Owner midtownOwner = new Owner("Midtown Owner", "midtown@example.com", "password", null);
        Franchise midtown = new Franchise("Midtown Location", midtownOwner);
        midtown.addTable(new Table(4));
        midtown.addTable(new Table(2));
        franchises.add(midtown);
        midtownOwner.setFranchise(midtown);
    }

    @GetMapping
    public List<Franchise> getAllFranchises() {
        return franchises;
    }

    @GetMapping("/{franchiseId}/tables")
    public List<Table> getTablesByFranchise(@PathVariable int franchiseId) {
        if (franchiseId >= 0 && franchiseId < franchises.size()) {
            return franchises.get(franchiseId).getTableAvailability();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Franchise not found");
    }
}