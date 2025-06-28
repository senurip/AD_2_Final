package com.example.parkingservice.service.impl;

import com.example.parkingservice.dto.ParkZoneDTO;
import com.example.parkingservice.dto.Response;
import com.example.parkingservice.entity.ParkZone;
import com.example.parkingservice.exception.OurException;
import com.example.parkingservice.repo.ParkZoneRepo;
import com.example.parkingservice.service.ParkZoneService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ParkZoneServiceImpl implements ParkZoneService {

    @Autowired
    private ParkZoneRepo parkZoneRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response createParkZone(ParkZoneDTO parkZoneDTO) {
        Response response = new Response();

        try {
            ParkZone parkZone = modelMapper.map(parkZoneDTO, ParkZone.class);
            ParkZone savedParkZone = parkZoneRepo.save(parkZone);
            response.setParkZone(modelMapper.map(savedParkZone, ParkZoneDTO.class));
            response.setStatusCode(201);
            response.setMessage("Park Zone created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage("Error creating Park Zone: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getParkZoneById(Long id) {
        Response response = new Response();
        try {
            ParkZone parkZone = parkZoneRepo.findById(id)
                    .orElseThrow(() -> new OurException("Park Zone not found with id: " + id));
            response.setParkZone(modelMapper.map(parkZone, ParkZoneDTO.class));
            response.setStatusCode(200);
            response.setMessage("Park Zone fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error fetching Park Zone: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllParkZones() {
    Response response = new Response();
    try {
        List<ParkZone> parkZones = parkZoneRepo.findAll();
        List<ParkZoneDTO> parkZoneDTOs = parkZones.stream()
                .map(parkZone -> modelMapper.map(parkZone, ParkZoneDTO.class))
                .collect(Collectors.toList());
        response.setStatusCode(200);
        response.setMessage("All Park Zones fetched successfully");
        response.setParkZoneList(parkZoneDTOs);
    }catch (OurException e) {
        response.setStatusCode(404);
        response.setMessage("Error fetching Park Zones: " + e.getMessage());
    } catch (Exception e) {
        response.setStatusCode(500);
        response.setMessage("An unexpected error occurred: " + e.getMessage());
    }
        return response;
    }

    @Override
    public Response updateParkZone(Long id, ParkZoneDTO parkZoneDTO) {
        Response response = new Response();
        try {
            ParkZone existingParkZone = parkZoneRepo.findById(id)
                    .orElseThrow(() -> new OurException("Park Zone not found with id: " + id));
            // Update fields
            existingParkZone.setName(parkZoneDTO.getName());
            existingParkZone.setLocation(parkZoneDTO.getLocation());
            // ...add other fields as needed...

            ParkZone updatedParkZone = parkZoneRepo.save(existingParkZone);
            response.setParkZone(modelMapper.map(updatedParkZone, ParkZoneDTO.class));
            response.setStatusCode(200);
            response.setMessage("Park Zone updated successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error updating Park Zone: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteParkZone(Long id) {
        Response response = new Response();
        try {
            ParkZone parkZone = parkZoneRepo.findById(id)
                    .orElseThrow(() -> new OurException("Park Zone not found with id: " + id));
            parkZoneRepo.delete(parkZone);
            response.setParkZone(modelMapper.map(parkZone, ParkZoneDTO.class));
            response.setStatusCode(200);
            response.setMessage("Park Zone deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage("Error deleting Park Zone: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
        }
        return response;
    }
}
