package com.example.field_service_management.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TechnicianRequest {

    @NotEmpty(message = "Name must not be empty")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @NotEmpty(message = "Phone must not be empty")
    private String phone;
}
