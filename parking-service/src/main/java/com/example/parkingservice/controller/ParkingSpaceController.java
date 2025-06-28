package com.example.parkingservice.controller;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parking-spaces")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @PostMapping("/create")
    public ResponseEntity<Response> createParkingSpace(@RequestBody ParkingSpaceDTO parkingSpaceDTO) {
        Response response = parkingSpaceService.createParkingSpace(parkingSpaceDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateParkingSpace(@PathVariable Long id, @RequestBody ParkingSpaceDTO parkingSpaceDTO) {
        Response response = parkingSpaceService.updateParkingSpace(id, parkingSpaceDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteParkingSpace(@PathVariable Long id) {
        Response response = parkingSpaceService.deleteParkingSpace(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getParkingSpaceById/{id}")
    public ResponseEntity<Response> getParkingSpaceById(@PathVariable Long id) {
        Response response = parkingSpaceService.getParkingSpaceById(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getAllParkingSpaces")
    public ResponseEntity<Response> getAllParkingSpaces() {
        Response response = parkingSpaceService.getAllParkingSpaces();
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }
}
