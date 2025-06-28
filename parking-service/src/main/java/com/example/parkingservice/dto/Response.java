package com.example.parkingservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private Double paymentAmount;

    private UserDTO user;
    private List<UserDTO> userList;

    private ParkZoneDTO parkZone;
    private List<ParkZoneDTO> parkZoneList;

    private ParkingSpaceDTO parkingSpace;
    private List<ParkingSpaceDTO> parkingSpaceList;

    private VehicleDTO vehicle;
    private List<VehicleDTO> vehicleList;

    private BookingDTO booking;
    private List<BookingDTO> bookingList;

    private PaymentDTO payment;
    private List<PaymentDTO> paymentList;

}
