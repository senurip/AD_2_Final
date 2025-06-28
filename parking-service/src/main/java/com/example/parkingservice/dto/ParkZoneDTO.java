package com.example.parkingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkZoneDTO {

    private Long parkZoneId;
    private String name;
    private String location;
    private Long ownerId;
    private List<ParkingSpaceDTO> parkingSpaces; // List of parking spaces in this zone

}
