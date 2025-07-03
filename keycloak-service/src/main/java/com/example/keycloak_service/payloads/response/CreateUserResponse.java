package com.example.keycloak_service.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CreateUserResponse {

    private String message;

    private String username;
}
