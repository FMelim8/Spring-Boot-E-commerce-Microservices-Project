package com.example.keycloak_service.payloads.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class AuthRequest {
    private final String grantType = "password";
    private final String clientId = "keycloak-client";
    @Value("${keycloak.client.secret}")
    private String clientSecret;
    private String username, password;
}
