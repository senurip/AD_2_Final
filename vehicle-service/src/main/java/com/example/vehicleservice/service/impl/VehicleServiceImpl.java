package com.example.vehicleservice.service.impl;

import com.example.vehicleservice.dto.Response;
import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.exception.OurException;
import com.example.vehicleservice.repo.VehicleRepo;
import com.example.vehicleservice.service.VehicleService;
import com.example.vehicleservice.service.client.UserClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepo vehicleRepo;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response createVehicle(VehicleDTO vehicleDTO) {
        Response response = new Response();
        try {
            if (vehicleRepo.existsByPlateNumber(vehicleDTO.getPlateNumber())) {
                throw new OurException("Vehicle with plate number " + vehicleDTO.getPlateNumber() + " already exists");
            }
            Vehicle vehicle = new Vehicle();
            vehicle.setPlateNumber(vehicleDTO.getPlateNumber());
            vehicle.setVehicleType(vehicleDTO.getVehicleType());
            vehicle.setUserId(vehicleDTO.getUserId());
            Vehicle savedVehicle = vehicleRepo.save(vehicle);
            VehicleDTO savedVehicleDTO = modelMapper.map(savedVehicle, VehicleDTO.class);
            response.setVehicle(savedVehicleDTO);
            response.setStatusCode(201);
            response.setMessage("Vehicle created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating vehicle: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Response response = new Response();
        try {
            Vehicle vehicle = vehicleRepo.findById(id)
                    .orElseThrow(() -> new OurException("Vehicle not found with id: " + id));
            vehicle.setPlateNumber(vehicle.getPlateNumber());
            vehicle.setVehicleType(vehicle.getVehicleType());
            vehicle.setUserId(vehicle.getUserId());
            Vehicle updateVahicle =vehicleRepo.save(vehicle);
            response.setStatusCode(200);
            response.setMessage("Vehicle updated successfully");
            response.setVehicle(modelMapper.map(updateVahicle, VehicleDTO.class));
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating vehicle: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteVehicle(Long id) {
        Response response = new Response();
        try {
            Vehicle vehicle = vehicleRepo.findById(id)
                    .orElseThrow(() -> new OurException("Vehicle not found with id: " + id));
            vehicleRepo.delete(vehicle);
            response.setVehicle(modelMapper.map(vehicle, VehicleDTO.class));
            response.setStatusCode(200);
            response.setMessage("Vehicle deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting vehicle: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getVehicleById(Long id) {
        Response response = new Response();
        try {
            Vehicle vehicle = vehicleRepo.findById(id)
                    .orElseThrow(() -> new OurException("Vehicle not found with id: " + id));
            VehicleDTO vehicleDTO = modelMapper.map(vehicle, VehicleDTO.class);
            response.setStatusCode(200);
            response.setMessage("Vehicle fetched successfully");
            response.setVehicle(vehicleDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching vehicle: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllVehicles() {
        Response response = new Response();
        try {
            List<Vehicle> vehicles = vehicleRepo.findAll();
            List<VehicleDTO> vehicleDTOs = vehicles.stream()
                    .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Vehicles fetched successfully");
            response.setVehicleList(vehicleDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching vehicles: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getVehiclesByUserId(Long userId) {
        Response response = new Response();
        try {
            List<Vehicle> vehicles = vehicleRepo.findByUserId(userId);
            if (vehicles == null || vehicles.isEmpty()) {
                throw new OurException("No vehicles found for userId: " + userId);
            }
            List<VehicleDTO> vehicleDTOs = vehicles.stream()
                    .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                    .collect(Collectors.toList());
            response.setStatusCode(200);
            response.setMessage("Vehicles fetched successfully for userId: " + userId);
            response.setVehicleList(vehicleDTOs);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching vehicles for userId: " + userId + " - " + e.getMessage());
        }
        return response;
    }
}
