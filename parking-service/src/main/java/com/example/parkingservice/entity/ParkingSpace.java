package com.example.parkingservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parking_spaces")
public class ParkingSpace {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parkingSpaceId;

    private VehicleType vehicleType;
    private boolean isAvailable; // true if available, false if occupied
    private Double hourlyRate; // Rate per hour for this parking space

    @ManyToOne
    @JoinColumn(name = "park_zone_id", nullable = false)
    private ParkZone parkZone; // Reference to the ParkZone entity

    @OneToMany(mappedBy = "parkingSpace", cascade = CascadeType.ALL)
    private List<Booking> bookings; // List of bookings for this parking space
}
