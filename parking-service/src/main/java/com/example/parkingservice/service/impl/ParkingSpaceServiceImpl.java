package com.example.parkingservice.service.impl;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.entity.ParkZone;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.exception.OurException;
import com.example.parkingservice.repo.ParkZoneRepo;
import com.example.parkingservice.repo.ParkingSpaceRepo;
import com.example.parkingservice.service.ParkingSpaceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepo parkingSpaceRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParkZoneRepo parkZoneRepo;

    @Override
    public Response createParkingSpace(ParkingSpaceDTO parkingSpaceDTO) {
        Response response = new Response();
        try {
            ParkZone parkZone = parkZoneRepo.findById(parkingSpaceDTO.getParkZoneId())
                    .orElseThrow(() -> new OurException("Park Zone not found with id: " + parkingSpaceDTO.getParkZoneId()));;

            ParkingSpace parkingSpace = new ParkingSpace();
            // Map DTO to entity
            parkingSpace.setVehicleType(parkingSpaceDTO.getVehicleType());
            parkingSpace.setHourlyRate(parkingSpaceDTO.getHourlyRate());
            parkingSpace.setAvailable(true);// Assuming new parking spaces are available by default
            parkingSpace.setParkZone(parkZone);

            ParkingSpace savedParkingSpace = parkingSpaceRepo.save(parkingSpace);
            response.setParkingSpace(modelMapper.map(savedParkingSpace, ParkingSpaceDTO.class));
            response.setStatusCode(201);
            response.setMessage("Parking Space created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error creating Parking Space: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getParkingSpaceById(Long id) {
        Response response = new Response();
        try {
            ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                    .orElseThrow(() -> new OurException("Parking Space not found with id: " + id));
            response.setParkingSpace(modelMapper.map(parkingSpace, ParkingSpaceDTO.class));
            response.setStatusCode(200);
            response.setMessage("Parking Space fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error fetching Parking Space: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllParkingSpaces() {
        Response response = new Response();
        try {
            List<ParkingSpace> parkingSpaces = parkingSpaceRepo.findAll();
            List<ParkingSpaceDTO> parkingSpaceDTOs = parkingSpaces.stream()
                    .map(space -> modelMapper.map(space, ParkingSpaceDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("All Parking Spaces fetched successfully");
            response.setParkingSpaceList(parkingSpaceDTOs);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error fetching Parking Spaces: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateParkingSpace(Long id, ParkingSpaceDTO parkingSpaceDTO) {
        Response response = new Response();
        try {
            ParkingSpace existingParkingSpace = parkingSpaceRepo.findById(id)
                    .orElseThrow(() -> new OurException("Parking Space not found with id: " + id));
            // Update fields
            existingParkingSpace.setVehicleType(parkingSpaceDTO.getVehicleType());
            existingParkingSpace.setHourlyRate(parkingSpaceDTO.getHourlyRate());
            existingParkingSpace.setAvailable(parkingSpaceDTO.isAvailable());
            // ...add other fields as needed...

            ParkingSpace updatedParkingSpace = parkingSpaceRepo.save(existingParkingSpace);
            response.setParkingSpace(modelMapper.map(updatedParkingSpace, ParkingSpaceDTO.class));
            response.setStatusCode(200);
            response.setMessage("Parking Space updated successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error updating Parking Space: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteParkingSpace(Long id) {
        Response response = new Response();
        try {
            ParkingSpace parkingSpace = parkingSpaceRepo.findById(id)
                    .orElseThrow(() -> new OurException("Parking Space not found with id: " + id));
            parkingSpaceRepo.delete(parkingSpace);
            response.setParkingSpace(modelMapper.map(parkingSpace, ParkingSpaceDTO.class));
            response.setStatusCode(200);
            response.setMessage("Parking Space deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error deleting Parking Space: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }
}
