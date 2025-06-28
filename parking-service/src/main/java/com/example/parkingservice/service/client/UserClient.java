package com.example.parkingservice.service.client;

import com.example.parkingservice.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/user-service/api/v1/users/getUserById/{id}")
    Response getUserById(@PathVariable("id") Long id);
}