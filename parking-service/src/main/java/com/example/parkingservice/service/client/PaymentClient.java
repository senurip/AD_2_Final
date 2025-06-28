package com.example.parkingservice.service.client;

import com.example.parkingservice.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @GetMapping("/payment-service/api/v1/payments/checkPayment")
    Response findPaymentByBookingIdAndUserId(@RequestParam Long bookingId, @RequestParam Long userId);
}
