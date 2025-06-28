package com.example.parkingservice.service.impl;

import com.example.parkingservice.dto.BookingDTO;
import com.example.parkingservice.dto.PaymentDTO;
import com.example.parkingservice.dto.PaymentStatus;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.entity.Booking;
import com.example.parkingservice.entity.BookingStatus;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.exception.OurException;
import com.example.parkingservice.repo.BookingRepo;
import com.example.parkingservice.repo.ParkingSpaceRepo;
import com.example.parkingservice.service.BookingService;
import com.example.parkingservice.service.client.PaymentClient;
import com.example.parkingservice.service.client.UserClient;
import com.example.parkingservice.service.client.VehicleClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepo bookingRepo;

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserClient userClient;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private VehicleClient vehicleClient;

    @Override
    public Response createBooking(BookingDTO bookingDTO) {
        Response response = new Response();
        try {

            // Validate parking space
            ParkingSpace parkingSpace = parkingSpaceRepo.findById(bookingDTO.getParkingSpaceId())
                    .orElseThrow(() -> new OurException("Parking Space not found with id: " + bookingDTO.getParkingSpaceId()));

            //check if parking space is available
            if (!parkingSpace.isAvailable()) {
                throw new OurException("Parking Space is not available");
            }

            // Validate user
            Response userResponse = userClient.getUserById(bookingDTO.getUserId());
            if (userResponse.getUser() == null) {
                throw new OurException("User not found with id: " + bookingDTO.getUserId());
            }
            // Validate vehicle
            Response vehicleResponse = vehicleClient.getVehicleById(bookingDTO.getVehicleId());
            if (vehicleResponse.getVehicle() == null) {
                throw new OurException("Vehicle not found with id: " + bookingDTO.getVehicleId());
            }

            // Mark parking space as unavailable
            parkingSpace.setAvailable(false);
            parkingSpaceRepo.save(parkingSpace);

            // Map DTO to entity
            Booking booking = new Booking();
            booking.setUserId(bookingDTO.getUserId());
            booking.setVehicleId(bookingDTO.getVehicleId());
            booking.setParkingSpace(parkingSpace);
            booking.setStartTime(LocalDateTime.now());
            booking.setStatus(bookingDTO.getStatus() != null ? bookingDTO.getStatus() : BookingStatus.RESERVED);

            Booking savedBooking = bookingRepo.save(booking);
            BookingDTO savedBookingDTO = modelMapper.map(savedBooking, BookingDTO.class);
            savedBookingDTO.setParkingSpaceId(parkingSpace.getParkingSpaceId());

            response.setBooking(savedBookingDTO);
            response.setStatusCode(201);
            response.setMessage("Booking created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error creating booking: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response completeBooking(Long bookingId) {
        Response response = new Response();
        try {
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking not found with id: " + bookingId));
            // Check if booking is in RESERVED status
            if (booking.getStatus() != BookingStatus.RESERVED) {
                throw new OurException("Booking is not in RESERVED status");
            }

            // Mark parking space as available
            ParkingSpace parkingSpace = booking.getParkingSpace();
            if (parkingSpace == null) {
                throw new OurException("Parking Space not found for this booking");
            }

            // Parking space release
            parkingSpace.setAvailable(true);
            parkingSpaceRepo.save(parkingSpace);

            LocalDateTime startTime = booking.getStartTime();
            LocalDateTime endTime = LocalDateTime.now();

            long durationInHours = calculateBookingDuration(startTime, endTime); // Calculate duration in hours

            // check if duration is zero , if so, cancel the booking
            if (durationInHours <= 0) {
                booking.setParkingSpace(parkingSpace);
                booking.setStatus(BookingStatus.CANCELLED);
                booking.setEndTime(endTime);
                Booking cancelBooking = bookingRepo.save(booking);

                BookingDTO cancelBookingDTO = modelMapper.map(cancelBooking, BookingDTO.class);
                cancelBookingDTO.setParkingSpaceId(cancelBooking.getParkingSpace().getParkingSpaceId());// return ParkSpaceId

                response.setBooking(cancelBookingDTO);
                response.setStatusCode(200);
                response.setMessage("Booking cancelled due to zero duration");
                return response;
            }

            // Save the updated booking, status to COMPLETED
            booking.setParkingSpace(parkingSpace); // Ensure parking space is set
            booking.setStatus(BookingStatus.COMPLETED);
            booking.setEndTime(endTime);
            Booking updatedBooking = bookingRepo.save(booking);

            BookingDTO bookingDTO = modelMapper.map(updatedBooking, BookingDTO.class);
            bookingDTO.setParkingSpaceId(updatedBooking.getParkingSpace().getParkingSpaceId());

            response.setBooking(bookingDTO);
            response.setStatusCode(200);
            response.setMessage("Booking completed successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error completing booking: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getBookingsByUserId(Long userId) {
        Response response = new Response();
        try {
            List<Booking> bookings = bookingRepo.findAll().stream()
                    .filter(b -> b.getUserId().equals(userId))
                    .collect(Collectors.toList());
            List<BookingDTO> bookingDTOs = bookings.stream()
                    .map(b -> {
                        BookingDTO dto = modelMapper.map(b, BookingDTO.class);
                        if (b.getParkingSpace() != null) {
                            dto.setParkingSpaceId(b.getParkingSpace().getParkingSpaceId());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            response.setBookingList(bookingDTOs);
            response.setStatusCode(200);
            response.setMessage("Bookings fetched successfully");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response calculateTotalAmount(Long bookingId) {
        Response response = new Response();
        try {
            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking not found with id: " + bookingId));

            // Check if booking is cancelled
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                throw new OurException("Booking is already cancelled, cannot calculate amount");
            }

            // Check if booking is completed
            if (booking.getStatus() != BookingStatus.COMPLETED) {
                throw new OurException("Booking is not completed yet");
            }

            ParkingSpace parkingSpace = booking.getParkingSpace();
            if (parkingSpace == null) {
                throw new OurException("Parking Space not found for this booking");
            }

            double ratePerHour = parkingSpace.getHourlyRate();

            LocalDateTime startTime = booking.getStartTime();
            LocalDateTime endTime = booking.getEndTime();

            long durationInHours = calculateBookingDuration(startTime, endTime);

            double totalAmount = durationInHours * ratePerHour;

            response.setPaymentAmount(totalAmount);
            response.setStatusCode(200);
            response.setMessage("Total amount calculated successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error calculating total amount: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response checkPaymentStatus(Long bookingId) {
        Response response = new Response();
        try {

            Booking booking = bookingRepo.findById(bookingId)
                    .orElseThrow(() -> new OurException("Booking not found with id: " + bookingId));


            if (booking.getStatus() == BookingStatus.CANCELLED) {
                throw new OurException("Booking is cancelled, does not require payment");
            }

            // Check payment status using paymentClient
            Response paymentResponse = paymentClient.findPaymentByBookingIdAndUserId(bookingId, booking.getUserId());

            PaymentDTO paymentDTO = modelMapper.map(paymentResponse.getPayment(), PaymentDTO.class);

            if (paymentDTO == null) {
                throw new OurException("Payment not found for this booking");
            }

            // Assume paymentResponse.getPaymentStatus() returns a PaymentStatus enum (SUCCESS, FAILED, PENDING)
            if (paymentDTO.getStatus() != PaymentStatus.SUCCESS) {
                throw new OurException("Payment is not successful. Current status: " + paymentDTO.getStatus());
            }

            booking.setPaymentId(paymentDTO.getPaymentId()); // Set payment ID if needed
            Booking updatedBooking = bookingRepo.save(booking);
            BookingDTO bookingDTO = modelMapper.map(updatedBooking, BookingDTO.class);

            response.setBooking(bookingDTO);
            response.setStatusCode(200);
            response.setMessage("Payment status checked successfully");

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error checking payment status: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    public long calculateBookingDuration(LocalDateTime startTime,LocalDateTime endTime) {

        // Calculate the duration in minutes
        long durationInMinutes = java.time.Duration.between(startTime, endTime).toMinutes();
        // Calculate the duration in hours, rounding up any partial hour
        long durationInHours = (long) Math.ceil(durationInMinutes / 60.0);
        return durationInHours;

    }
}
