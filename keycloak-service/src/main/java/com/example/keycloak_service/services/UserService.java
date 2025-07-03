package com.example.keycloak_service.services;

import com.example.keycloak_service.models.User;
import com.example.keycloak_service.payloads.request.CreateUserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<?> login(String username, String password);

    ResponseEntity<?> createKeycloakUser(@Valid CreateUserRequest createUserRequest);

    ResponseEntity<?> getCurrentUserInfo();

    List<User> getUsersByIds(List<Long> userIds);
}
