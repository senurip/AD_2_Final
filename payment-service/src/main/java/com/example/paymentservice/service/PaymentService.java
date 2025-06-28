package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.Response;

public interface PaymentService {

    Response createPayment(PaymentDTO paymentDTO);
    Response getPaymentById(Long paymentId);
    Response getPaymentsByUserId(Long userId);
    Response getAllPayments();
    Response findPaymentIdByBookingIdAndUserId(Long bookingId, Long userId);
}
