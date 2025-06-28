package com.example.userservice.dto;

import com.example.userservice.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String userName;
    private String password;
    private String email;
    private UserRole role; // DRIVER / PARKING_OWNER
    private String phone;

}
