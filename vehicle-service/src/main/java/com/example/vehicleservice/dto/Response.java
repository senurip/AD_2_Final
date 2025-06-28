package com.example.vehicleservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;

    private UserDTO user;
    private List<UserDTO> userList;

    private VehicleDTO vehicle;
    private List<VehicleDTO> vehicleList;
}
