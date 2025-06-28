package com.example.paymentservice.service.impl;

import com.example.paymentservice.dto.PaymentDTO;
import com.example.paymentservice.dto.Response;
import com.example.paymentservice.entity.Payment;
import com.example.paymentservice.exception.OurException;
import com.example.paymentservice.repo.PaymentRepo;
import com.example.paymentservice.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.paymentservice.entity.PaymentStatus.SUCCESS;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepo paymentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response createPayment(PaymentDTO paymentDTO) {
        Response response = new Response();
        try {
            Payment payment = new Payment();
            payment.setBookingId(paymentDTO.getBookingId());
            payment.setTransactionId(UUID.randomUUID().toString());
            payment.setUserId(paymentDTO.getUserId());
            payment.setAmount(paymentDTO.getAmount());
            payment.setStatus(SUCCESS);  // Simulated as always successful

            Payment savedPayment = paymentRepo.save(payment);
            PaymentDTO savedPaymentDTO = modelMapper.map(savedPayment, PaymentDTO.class);

            response.setPayment(savedPaymentDTO);
            response.setStatusCode(201);
            response.setMessage("Payment created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Payment creation failed: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getPaymentById(Long paymentId) {
        Response response = new Response();
        try {
            Payment payment = paymentRepo.findById(paymentId)
                    .orElseThrow(() -> new OurException("Payment not found with id: " + paymentId));
            PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
            response.setPayment(paymentDTO);
            response.setStatusCode(200);
            response.setMessage("Payment fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Payment not found: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getPaymentsByUserId(Long userId) {
        Response response = new Response();
        try {
            List<Payment> payments = paymentRepo.findAll()
                    .stream()
                    .filter(p -> p.getUserId().equals(userId))
                    .collect(Collectors.toList());
            List<PaymentDTO> paymentDTOs = payments.stream()
                    .map(payment -> modelMapper.map(payment, PaymentDTO.class))
                    .collect(Collectors.toList());
            response.setPayments(paymentDTOs);
            response.setStatusCode(200);
            response.setMessage("Payments fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Payments not found: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllPayments() {
        return null;
    }

    @Override
    public Response findPaymentIdByBookingIdAndUserId(Long bookingId, Long userId) {
    Response response = new Response();
    try {
        Long paymentId = paymentRepo.findPaymentIdByBookingIdAndUserId(bookingId, userId);

        if (paymentId == null) {
            throw new OurException("Payment not found for bookingId: " + bookingId + " and userId: " + userId);
        }

        Payment payment = paymentRepo.findById(paymentId)
                .orElseThrow(() -> new OurException("Payment not found for bookingId: " + bookingId + " and userId: " + userId));
        PaymentDTO paymentDTO = modelMapper.map(payment, PaymentDTO.class);
        response.setPayment(paymentDTO);
        response.setStatusCode(200);
        response.setMessage("Payment details fetched successfully");
    } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Payment status not found: " + e.getMessage());
    } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
    }
    return response;
    }
}
