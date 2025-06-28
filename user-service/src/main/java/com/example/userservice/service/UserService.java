package com.example.userservice.service;


import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.Response;
import com.example.userservice.dto.UserDTO;

public interface UserService {

    Response createUser(UserDTO userDTO);

    Response login(LoginRequest loginRequest);

    Response getUserById(Long id);

    Response updateUser(Long id, String name, String phone);

    Response deleteUser(Long id);

    Response getAllUsers();
}
