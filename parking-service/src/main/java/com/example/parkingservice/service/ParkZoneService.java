package com.example.parkingservice.service;

import com.example.parkingservice.dto.ParkZoneDTO;
import com.example.parkingservice.dto.Response;

public interface ParkZoneService {

    Response createParkZone(ParkZoneDTO parkZoneDTO);
    Response getParkZoneById(Long id);
    Response getAllParkZones();
    Response updateParkZone(Long id, ParkZoneDTO parkZoneDTO);
    Response deleteParkZone(Long id);
}
