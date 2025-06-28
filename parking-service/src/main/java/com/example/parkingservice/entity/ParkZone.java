package com.example.parkingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "park_zones")
public class ParkZone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parkZoneId;
    private String name;
    private String location;

    @Column(nullable = false)
    private Long ownerId;

    @OneToMany(mappedBy = "parkZone", cascade = CascadeType.ALL)
    private List<ParkingSpace> parkingSpaces; // List of parking spaces in this zone
}
