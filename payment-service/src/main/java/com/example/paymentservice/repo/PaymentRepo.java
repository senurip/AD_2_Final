package com.example.paymentservice.repo;

import com.example.paymentservice.dto.Response;
import com.example.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Response findByUserId(Long userId);

    @Query("SELECT p.paymentId FROM Payment p WHERE p.bookingId = :bookingId AND p.userId = :userId")
    Long findPaymentIdByBookingIdAndUserId(@Param("bookingId") Long bookingId, @Param("userId") Long userId);
}

