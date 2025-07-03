package com.example.keycloak_service.config;

import com.example.sharedlib.exception.ServiceCustomException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Getter
@Configuration
@NoArgsConstructor
public class KeycloakProvider {

    @Value("${keycloak.auth-server-url}")
    public String serverUrl;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.admin-cli.name}")
    public String adminClientId;

    @Value("${keycloak.admin-cli.secret}")
    public String adminClientSecret;

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Bean
    public Keycloak keycloak(){
        Keycloak keycloak = null;
        try {
            keycloak = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverUrl)
                    .clientId(adminClientId)
                    .clientSecret(adminClientSecret)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .build();
        } catch (Exception e) {
            logger.error(
                    "Connection Error",
                    kv("message", e.getMessage())
            );

            throw new ServiceCustomException(
                    "Service Unavailable",
                    "Service temporarily unavailable",
                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                    ""
            );
        }

        return keycloak;
    }

}