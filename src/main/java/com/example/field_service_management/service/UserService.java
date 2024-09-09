package com.example.field_service_management.service;

import com.example.field_service_management.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    User saveUser(User user);
}
