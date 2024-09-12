package com.example.field_service_management.utils;

import com.example.field_service_management.dto.UserResponseDTO;
import com.example.field_service_management.model.User;
import com.example.field_service_management.model.UserRole;

import java.util.Set;
import java.util.stream.Collectors;

public class UserUtils {

    public static UserResponseDTO toUserResponseDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(UserRole::getName)
                .collect(Collectors.toSet());
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                roleNames
        );
    }
}
