package com.example.keycloak_service.payloads.response;

import com.example.sharedlib.annotations.Sensitive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data @Builder
@RequiredArgsConstructor @AllArgsConstructor
public class LoginResponse {

    @Sensitive
    @JsonProperty("access_token")
    private String accessToken;

    @Sensitive
    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("refresh_expires_in")
    private long refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("session_state")
    private String sessionState;

    @JsonProperty("scope")
    private String scope;
}
