package com.example.field_service_management.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SyntaxRequest {

    @NotEmpty(message = "Syntax must not be empty")
    private String syntax;

    @NotEmpty(message = "module must not be empty")
    private String module;

    private FieldRequest assignedField;

    private Map<String,String> variablesWithType;

    @Getter
    @Setter
    public static class FieldRequest {

        private String fieldName;

        private String dataType;
    }
}
