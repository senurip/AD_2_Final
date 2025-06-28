package com.example.parkingservice.controller;

import com.example.parkingservice.dto.ParkZoneDTO;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.dto.UserDTO;
import com.example.parkingservice.service.ParkZoneService;
import com.example.parkingservice.service.client.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/park-zones")
public class ParkZoneController {

    @Autowired
    private ParkZoneService parkZoneService;

    @Autowired
    private UserClient userClient;

//    @PreAuthorize("hasAuthority('PARKING_OWNER')")
    @PostMapping("/create")
    public ResponseEntity<Response> createParkZone(@RequestBody ParkZoneDTO parkZoneDTO) {
        Response response = parkZoneService.createParkZone(parkZoneDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateParkZone(@PathVariable Long id, @RequestBody ParkZoneDTO parkZoneDTO) {
        Response response = parkZoneService.updateParkZone(id, parkZoneDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteParkZone(@PathVariable Long id) {
        Response response = parkZoneService.deleteParkZone(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getParkZoneById/{id}")
    public ResponseEntity<Response> getParkZoneById(@PathVariable Long id) {
        Response response = parkZoneService.getParkZoneById(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getAllParkZones")
    public ResponseEntity<Response> getAllParkZones() {
        Response response = parkZoneService.getAllParkZones();
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }
}
