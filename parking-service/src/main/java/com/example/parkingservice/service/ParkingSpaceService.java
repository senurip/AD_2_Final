package com.example.parkingservice.service;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.dto.Response;

public interface ParkingSpaceService {

    Response createParkingSpace(ParkingSpaceDTO parkingSpaceDTO);
    Response getParkingSpaceById(Long id);
    Response getAllParkingSpaces();
    Response updateParkingSpace(Long id, ParkingSpaceDTO parkingSpaceDTO);
    Response deleteParkingSpace(Long id);
}
