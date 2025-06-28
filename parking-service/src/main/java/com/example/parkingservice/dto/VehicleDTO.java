package com.example.parkingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {

    private Long vehicleId;
    private String plateNumber;
    private VehicleType vehicleType;
    private Long userId;
}
