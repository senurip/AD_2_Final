package com.example.userservice.controller;

import com.example.userservice.dto.LoginRequest;
import com.example.userservice.dto.Response;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.service.CustomUserDetailsService;
import com.example.userservice.service.UserService;
import com.example.userservice.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserDTO userDTO) {
        Response response = userServiceImpl.createUser(userDTO);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Response> loginUser(@RequestBody LoginRequest loginRequest) {
        Response response = userServiceImpl.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }


    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        Response response = userServiceImpl.getUserById(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<Response> getAllUsers() {
        Response response = userServiceImpl.getAllUsers();
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable Long id,
                                               @RequestParam(value = "name", required = false) String name,
                                               @RequestParam(value = "phone",required = false) String phone) {
        Response response = userServiceImpl.updateUser(id, name, phone);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable Long id) {
        Response response = userServiceImpl.deleteUser(id);
        return ResponseEntity.status(response.getStatusCode() != 0 ? response.getStatusCode() : 200).body(response);
    }

    @GetMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token, @RequestParam String username) {
        try {
            // FIX: Trim whitespace from the username to prevent lookup errors.
            username = username.trim();
            System.out.println("USERCONTROLLER: /validate-token called for username: [" + username + "]");
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
            boolean isValid = jwtUtils.isValidToken(token, userDetails);
            return ResponseEntity.ok(isValid);
        } catch (Exception e) {
            // If user not found or any other error, token is invalid.
            System.out.println("USERCONTROLLER : Error validating token for username [" + username + "]: " + e.getMessage());
            return ResponseEntity.ok(false);
        }
    }
}
