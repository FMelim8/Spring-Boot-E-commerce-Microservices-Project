package com.example.keycloak_service.payloads.request;

import com.example.sharedlib.annotations.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    private String username;

    @Sensitive
    private String password;
}
