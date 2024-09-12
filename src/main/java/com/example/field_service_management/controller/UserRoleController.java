package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
import com.example.field_service_management.dto.UserResponseDTO;
import com.example.field_service_management.model.User;
import com.example.field_service_management.model.UserRole;
import com.example.field_service_management.service.UserRoleService;
import com.example.field_service_management.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserRole>> createRole(@RequestBody UserRole role) {
        if (userRoleService.findByName(role.getName()).isPresent()) {
            ApiResponse<UserRole> response = new ApiResponse<>("Fail", "Role already exists", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        UserRole newRole = userRoleService.createRole(role);
        ApiResponse<UserRole> response = new ApiResponse<>("Success", "Role Created", newRole);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/assign/{username}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> assignRoleToUser(
            @PathVariable String username, @RequestBody UserRole role) {
            String roleName = role.getName();
            User user = userRoleService.assignRoleToUser(username, roleName);
            UserResponseDTO userResponseDTO = UserUtils.toUserResponseDTO(user);
            ApiResponse<UserResponseDTO> response = new ApiResponse<>("Success", "Role %s successfully assigned to user %s".formatted(roleName, username), userResponseDTO);
            return ResponseEntity.ok(response);
    }
}
