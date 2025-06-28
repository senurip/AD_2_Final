package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.Response;
import com.example.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Response> createPayment(@RequestBody PaymentDTO paymentDTO) {
        Response response = paymentService.createPayment(paymentDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @GetMapping("/getPaymentById/{id}")
    public ResponseEntity<Response> getPaymentById(@PathVariable Long id) {
        Response response = paymentService.getPaymentById(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getPaymentsByUserId/{userId}")
    public ResponseEntity<Response> getPaymentsByUserId(@PathVariable Long userId) {
        Response response = paymentService.getPaymentsByUserId(userId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/checkPayment")
    public ResponseEntity<Response> findPaymentByBookingIdAndUserId(
            @RequestParam Long bookingId,
            @RequestParam Long userId) {
        Response response = paymentService.findPaymentIdByBookingIdAndUserId(bookingId, userId);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }
}
