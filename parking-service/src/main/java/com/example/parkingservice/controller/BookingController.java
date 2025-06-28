package com.example.parkingservice.controller;

import com.example.parkingservice.dto.BookingDTO;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<Response> createBooking(@RequestBody BookingDTO bookingDTO) {
        Response response = bookingService.createBooking(bookingDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @PutMapping("/complete/{bookingId}")
    public ResponseEntity<Response> completeBooking(@PathVariable Long bookingId) {
        Response response = bookingService.completeBooking(bookingId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    public ResponseEntity<Response> getBookingsByUserId(@PathVariable Long userId) {
        Response response = bookingService.getBookingsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/calculateTotalAmount/{bookingId}")
    public ResponseEntity<Response> calculateTotalAmount(@PathVariable Long bookingId) {
        Response response = bookingService.calculateTotalAmount(bookingId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/checkPaymentByBookingId/{bookingId}")
    public ResponseEntity<Response> checkPaymentStatus(@PathVariable Long bookingId) {
        Response response = bookingService.checkPaymentStatus(bookingId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }
}
