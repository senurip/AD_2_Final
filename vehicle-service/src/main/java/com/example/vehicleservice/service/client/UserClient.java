package com.example.vehicleservice.service.client;

import com.example.vehicleservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")  // Service name registered in Eureka
public interface UserClient {
    @GetMapping("/getUserById/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);
 }
