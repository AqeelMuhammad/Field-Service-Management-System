package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
import com.example.field_service_management.dto.UserResponseDTO;
import com.example.field_service_management.model.User;
import com.example.field_service_management.model.UserRole;
import com.example.field_service_management.service.UserRoleService;
import com.example.field_service_management.service.UserService;
import com.example.field_service_management.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseDTO>> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>("Fail", "User already exists", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            UserRole defaultRole = userRoleService.findByName("ROLE_USER").orElseThrow(
                    () -> new RuntimeException("Default role not found"));
            user.setRoles(Set.of(defaultRole));
        }
        User savedUser = userService.saveUser(user);
        UserResponseDTO userResponseDTO = UserUtils.toUserResponseDTO(savedUser);
        ApiResponse<UserResponseDTO> response = new ApiResponse<>("Success", "User Registration Success", userResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> getUser(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserResponseDTO userResponseDTO = UserUtils.toUserResponseDTO(user);
            ApiResponse<UserResponseDTO> response = new ApiResponse<>("Success", "User details fetched successfully", userResponseDTO);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserResponseDTO> response = new ApiResponse<>("Fail", "User not found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}
