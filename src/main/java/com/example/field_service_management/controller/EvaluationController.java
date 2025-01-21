package com.example.field_service_management.controller;

import com.example.field_service_management.dto.SyntaxRequest;
import com.ezylang.evalex.Expression;
import com.ezylang.evalex.config.ExpressionConfiguration;
import com.ezylang.evalex.data.EvaluationValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.time.Instant;
import java.time.Duration;

@RestController
@RequestMapping("/api/eval")
public class EvaluationController {

    private final ExpressionConfiguration config;
    private final Map<String, Object> mockData;

    public EvaluationController() throws Exception {
        this.config = ExpressionConfiguration.defaultConfiguration().toBuilder()
                .singleQuoteStringLiteralsAllowed(true)
                .build();
        this.mockData = loadMockData();
    }

    private static final Map<String, Object> PREDEFINED_VALUES = new HashMap<>();
    private static final Map<String, Class<?>> TYPE_MAPPING = new HashMap<>();
    
    static {
        PREDEFINED_VALUES.put("STRING", "SampleString");
        PREDEFINED_VALUES.put("INTEGER", 11);
        PREDEFINED_VALUES.put("LONG", 1L);
        PREDEFINED_VALUES.put("BOOLEAN", true);
        PREDEFINED_VALUES.put("INSTANT", Instant.parse("2025-01-22T11:20:00.00Z"));
        PREDEFINED_VALUES.put("DURATION", Duration.ofDays(3));
        PREDEFINED_VALUES.put("UUID", "e0a3b3f3-db9a-43a0-b3b3-cc5dee0ad900");
        PREDEFINED_VALUES.put("FLOAT", 3.14f);
        PREDEFINED_VALUES.put("DOUBLE", 3.14159);
        PREDEFINED_VALUES.put("LOCALDATE", "2025-01-16");
        PREDEFINED_VALUES.put("LOCALTIME", "12:34:56");
        
        TYPE_MAPPING.put("Instant", Instant.class);
        TYPE_MAPPING.put("String", String.class);
        TYPE_MAPPING.put("Boolean", Boolean.class);
        TYPE_MAPPING.put("Number", BigDecimal.class);
        TYPE_MAPPING.put("BigDecimal", BigDecimal.class);
        TYPE_MAPPING.put("BigInteger", BigInteger.class);
        TYPE_MAPPING.put("Long", Long.class);
        TYPE_MAPPING.put("Integer", Integer.class);
        TYPE_MAPPING.put("Double", Double.class);
        TYPE_MAPPING.put("Float", Float.class);
        TYPE_MAPPING.put("Short", Short.class);
        TYPE_MAPPING.put("Byte", Byte.class);
        TYPE_MAPPING.put("Duration", Duration.class);
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateExpression(@Valid @RequestBody SyntaxRequest request) {
        try {
            Map<String, Object> variables = new HashMap<>();
            if (request.getVariablesWithType() != null && !request.getVariablesWithType().isEmpty()){
                for (Map.Entry<String, String> entry : request.getVariablesWithType().entrySet()) {
                    Object predefinedValue = PREDEFINED_VALUES.get(entry.getValue().toUpperCase());
                    if (predefinedValue == null) throw new IllegalArgumentException("No predefined value for data type: " + entry.getValue());
                    addNestedVariable(variables, entry.getKey(), predefinedValue);
                }
            }
            Expression expression = new Expression(request.getSyntax(), config);
            variables.forEach(expression::with);
            expression.validate();
            EvaluationValue result = expression.evaluate();
            System.out.println("Result: " + result.getValue() + ", Data Type: " + result.getDataType());
            if (isTypeMatch(result, request.getAssignedField().getDataType())) {
                return ResponseEntity.ok("The syntax is valid, and the result matches the expected data type.");
            } else {
                return ResponseEntity.badRequest().body("Result type does not match the assigned field's expected type.");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Validation error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing expression: " + e.getMessage());
        }
    }

    private boolean isTypeMatch(EvaluationValue value, String expectedType) {
        if (value == null || expectedType == null) {
            throw new IllegalArgumentException("Value or expected type cannot be null");
        }

        Class<?> expectedClass = TYPE_MAPPING.get(expectedType);
        if (expectedClass == null) {
            throw new IllegalArgumentException("Unsupported data type: " + expectedType);
        }
        Object actualValue = value.getValue();
        if (actualValue instanceof BigDecimal && Number.class.isAssignableFrom(expectedClass)) {
            BigDecimal bigDecimalValue = (BigDecimal) actualValue;
            try {
                if (expectedClass == Integer.class) {
                    if (bigDecimalValue.scale() == 0) {
                        bigDecimalValue.intValueExact();
                    }
                    else return false;
                } else if (expectedClass == Long.class) {
                    bigDecimalValue.longValueExact();
                } else if (expectedClass == Double.class) {
                    bigDecimalValue.doubleValue();
                } else if (expectedClass == Float.class) {
                    bigDecimalValue.floatValue();
                } else if (expectedClass == Short.class) {
                    bigDecimalValue.shortValueExact();
                } else if (expectedClass == Byte.class) {
                    bigDecimalValue.byteValueExact();
                } else {
                    return false;
                }
                return true;
            } catch (ArithmeticException e) {
                return false;
            }
        }
        return expectedClass.isInstance(value.getValue());
    }

    private void addNestedVariable(Map<String, Object> variables, String variableName, Object value) {
        if (variableName.contains(".")) {
            String[] parts = variableName.split("\\.");
            Map<String, Object> current = variables;

            for (int i = 0; i < parts.length - 1; i++) {
                current = (Map<String, Object>) current.computeIfAbsent(parts[i], k -> new HashMap<>());
            }
            current.put(parts[parts.length - 1], value);
        } else {
            variables.put(variableName, value);
        }
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Object> evaluateExpression(@Valid @RequestBody SyntaxRequest request) {
        try {
            Map<String, Object> variables = new HashMap<>();
            if (request.getVariablesWithType() != null && !request.getVariablesWithType().isEmpty()) {
                for (Map.Entry<String, String> entry : request.getVariablesWithType().entrySet()) {
                    String variableName = entry.getKey();
                    Object value = getMockValue(variableName);
                    addNestedVariable(variables, variableName, value);
                }
            }

            Expression expression = new Expression(request.getSyntax(), config);
            variables.forEach(expression::with);

            // Evaluate the expression
            EvaluationValue result = expression.evaluate();
            return ResponseEntity.ok(result.getValue());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error evaluating expression: " + e.getMessage());
        }
    }

    private Object getMockValue(String variableName) {
        String[] parts = variableName.split("\\.");
        Map<String, Object> current = mockData;

        for (int i = 0; i < parts.length; i++) {
            Object value = current.get(parts[i]);
            if (value == null) {
                throw new IllegalArgumentException("Mock value not found for variable: " + variableName);
            }
            if (i == parts.length - 1) {
                return value;
            }

            // If value is not a Map, we cannot proceed further
            if (!(value instanceof Map)) {
                throw new IllegalArgumentException("Mock value not found for variable: " + variableName);
            }

            current = (Map<String, Object>) value;
        }
        return current;
    }

    private Map<String,  Object> loadMockData() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/mockData.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("mockData.json not found in classpath");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, new TypeReference<>() {});
        }
    }

//    private Map<String, Object> loadMockDataForModule(String module) {
//        try (InputStream inputStream = getClass().getResourceAsStream("./mockData.json")) {
//            if (inputStream == null) {
//                throw new FileNotFoundException("mockData.json not found in classpath");
//            }
//            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Map<String, Object>> allMockData = mapper.readValue(inputStream, new TypeReference<>() {});
//            return allMockData.getOrDefault(module, new HashMap<>());
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load mock data: " + e.getMessage(), e);
//        }
//    }

}
