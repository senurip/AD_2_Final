package com.example.userservice.service.impl;

import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.Response;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.OurException;
import com.example.userservice.repo.UserRepo;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JWTUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response createUser(UserDTO userDTO) {
        Response response = new Response();
        try {
            if (userRepo.existsByEmail(userDTO.getEmail())) {
                throw new OurException("Email already exists: " + userDTO.getEmail());
            }
            User user = new User();
            user.setUserName(userDTO.getUserName());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setRole(userDTO.getRole());
            user.setPhone(userDTO.getPhone());
            userRepo.save(user);
            response.setUser(modelMapper.map(user, UserDTO.class));
            response.setStatusCode(201);
            response.setMessage("User created successfully");
        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error creating user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepo.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User not found with email: " + loginRequest.getEmail()));

            System.out.println(user);
            var token = jwtUtils.generateToken(user);
            System.out.println("Generated Token: " + token);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(String.valueOf(user.getRole()));
            response.setExpirationTime("7 Days");
            response.setMessage("Login successful");
        } catch (BadCredentialsException | InternalAuthenticationServiceException | UsernameNotFoundException e) {
            response.setStatusCode(401);
            response.setMessage("Invalid email or password");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error during User login: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(Long id) {
        Response response = new Response();
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new OurException("User not found with id: " + id));
            response.setUser(modelMapper.map(user, UserDTO.class));
            response.setStatusCode(200);
            response.setMessage("User fetched successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateUser(Long id, String name, String phone) {
        Response response = new Response();
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new OurException("User not found with id: " + id));
            user.setUserName(name);
            user.setPhone(phone);
            userRepo.save(user);
            response.setUser(modelMapper.map(user, UserDTO.class));
            response.setStatusCode(200);
            response.setMessage("User updated successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(Long id) {
        Response response = new Response();
        try {
            User user = userRepo.findById(id)
                    .orElseThrow(() -> new OurException("User not found with id: " + id));
            userRepo.delete(user);
            response.setUser(modelMapper.map(user, UserDTO.class));
            response.setStatusCode(200);
            response.setMessage("User deleted successfully");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting user: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {
        Response response = new Response();
        try {
            List<User> users = userRepo.findAll();
            List<UserDTO> userDTOs = users.stream()
                    .map(user -> modelMapper.map(user, UserDTO.class))
                    .collect(Collectors.toList());
            response.setUserList(userDTOs);
            response.setStatusCode(200);
            response.setMessage("Users fetched successfully");
        }catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error fetching users: " + e.getMessage());
        }
            return response;
        }
    }

