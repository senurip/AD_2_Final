package com.example.parkingservice.dto;

import com.example.parkingservice.entity.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingSpaceDTO {

    private Long parkingSpaceId;
    private VehicleType vehicleType;
    private boolean isAvailable;
    private Double hourlyRate;
    private Long parkZoneId; // Reference to the ParkZone entity
}
