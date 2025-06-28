package com.example.parkingservice.dto;

import com.example.parkingservice.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private Long bookingId;
    private Long userId;
    private Long vehicleId;
    private Long paymentId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingStatus status; // RESERVED, COMPLETED, CANCELLED
    private Long parkingSpaceId; // Reference to the ParkingSpace entity
}
