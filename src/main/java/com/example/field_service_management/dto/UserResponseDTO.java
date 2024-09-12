package com.example.field_service_management.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class UserResponseDTO {
    private Long id;
    private String username;
    private Set<String> roles;

    public UserResponseDTO(Long id, String username, Set<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

}
