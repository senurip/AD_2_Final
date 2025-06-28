package com.example.vehicleservice.dto;

import com.example.vehicleservice.entity.VehicleType;
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
