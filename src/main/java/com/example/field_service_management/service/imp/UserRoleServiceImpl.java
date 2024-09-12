package com.example.field_service_management.service.imp;

import com.example.field_service_management.exception.CustomApiException;
import com.example.field_service_management.model.UserRole;
import com.example.field_service_management.model.User;
import com.example.field_service_management.repository.UserRepository;
import com.example.field_service_management.repository.UserRoleRepository;
import com.example.field_service_management.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserRoleServiceImpl(UserRoleRepository userRoleRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Optional<UserRole> findByName(String name) {
        return userRoleRepository.findByName(name);
    }

    @Override
    public UserRole createRole(UserRole role) {
        return userRoleRepository.save(role);
    }

    @Override
    public User assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new CustomApiException("User with username '%s' not found".formatted(username), HttpStatus.NOT_FOUND));

        UserRole role = userRoleRepository.findByName(roleName).orElseThrow(
                () -> new CustomApiException("Role '%s' not found".formatted(roleName), HttpStatus.NOT_FOUND));

        if (user.getRoles().contains(role)) {
            throw new CustomApiException("Role '%s' already assigned to user '%s'".formatted(roleName, username), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        user.getRoles().add(role);
        return userRepository.save(user);
    }

}
