package com.example.parkingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Long paymentId;

    private String transactionId;
    private Long userId;
    private Long bookingId;
    private Double amount;
    private PaymentStatus status; // SUCCESS, FAILED, PENDING
    private LocalDateTime createdAt;
}
