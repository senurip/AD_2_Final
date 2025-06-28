package com.example.vehicleservice.controller;

import com.example.vehicleservice.dto.Response;
import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/create")
    public ResponseEntity<Response> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        Response response = vehicleService.createVehicle(vehicleDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO vehicleDTO) {
        Response response = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteVehicle(@PathVariable Long id) {
        Response response = vehicleService.deleteVehicle(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getVehicleById/{id}")
    public ResponseEntity<Response> getVehicleById(@PathVariable Long id) {
        Response response = vehicleService.getVehicleById(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getAllVehicles")
    public ResponseEntity<Response> getAllVehicles() {
        Response response = vehicleService.getAllVehicles();
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response> getVehiclesByUserId(@PathVariable Long userId) {
        Response response = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }
}
