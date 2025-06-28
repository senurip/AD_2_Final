package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.Response;
import com.example.vehicleservice.dto.VehicleDTO;

public interface VehicleService {
    Response createVehicle(VehicleDTO vehicleDTO);
    Response updateVehicle(Long id, VehicleDTO vehicleDTO);
    Response deleteVehicle(Long id);
    Response getVehicleById(Long id);
    Response getAllVehicles();
    Response getVehiclesByUserId(Long userId);
}
