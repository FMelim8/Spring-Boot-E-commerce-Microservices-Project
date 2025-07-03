package com.example.keycloak_service.payloads.request;

import com.example.sharedlib.annotations.Sensitive;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateUserRequest {
    @NotBlank
    private String username;

    @Sensitive
    @NotBlank
    private String password;

    @NotBlank
    private String email;

    private String firstName;

    private String lastName;
}
