package com.example.parkingservice.service.client;

import com.example.parkingservice.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "vehicle-service")
public interface VehicleClient {
    @GetMapping("/vehicle-service/api/v1/vehicles/getVehicleById/{id}")
    Response getVehicleById(@PathVariable("id") Long id);
}
