package com.example.field_service_management.controller;

import com.example.field_service_management.dto.ApiResponse;
import com.example.field_service_management.service.imp.CustomUserDetailsServiceImpl;
import com.example.field_service_management.security.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/register")
    public ResponseEntity<ApiResponse<?>> generateNewSecretKey() throws JsonProcessingException {
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated Key: " + encodedKey);
        byte[] keyBytes = secretKey.getEncoded();
        System.out.println("Key Length: " + keyBytes.length * 8 + " bits");
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("encodedKey", encodedKey);
        responseMap.put("keyBytes", keyBytes);
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(responseMap);
        ApiResponse<?> response = new ApiResponse<>("Success", "New Secret Key Created", jsonResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            ApiResponse<?> response = new ApiResponse<>("Fail", "Username or Password cannot be empty", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            String token = jwtTokenUtil.generateToken(userDetails.getUsername());

            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("token", token);

            ApiResponse<?> response = new ApiResponse<>("Success", "User Login Success", responseBody);
            return ResponseEntity.ok(response);

        } catch (AuthenticationException e) {
            ApiResponse<?> response = new ApiResponse<>("Fail", "Invalid username or password", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}
