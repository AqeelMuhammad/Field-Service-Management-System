package com.example.field_service_management.service;

import com.example.field_service_management.model.User;
import com.example.field_service_management.model.UserRole;

import java.util.Optional;

public interface UserRoleService {
    Optional<UserRole> findByName(String name);

    UserRole createRole(UserRole role);

    User assignRoleToUser(String username, String roleName);
}
