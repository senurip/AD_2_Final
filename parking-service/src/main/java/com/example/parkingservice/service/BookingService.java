package com.example.parkingservice.service;

import com.example.parkingservice.dto.BookingDTO;
import com.example.parkingservice.dto.Response;

public interface BookingService {

    Response createBooking(BookingDTO bookingDTO);
    Response completeBooking(Long bookingId);
    Response getBookingsByUserId(Long userId);
    Response calculateTotalAmount(Long bookingId);
    Response checkPaymentStatus(Long bookingId);
}
