package com.example.keycloak_service;

import com.example.keycloak_service.config.KeycloakProvider;
import com.example.keycloak_service.payloads.request.CreateUserRequest;
import com.example.keycloak_service.payloads.request.LoginRequest;
import com.example.keycloak_service.repository.UserRepository;
import com.example.keycloak_service.services.impl.UserServiceImpl;
import com.example.sharedlib.exception.ServiceCustomException;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Testcontainers
@ExtendWith(MockitoExtension.class)
public class KeycloakServiceTest {

    @Mock
    UserRepository userRepository;

    private UserServiceImpl userService;

    @Container
    static KeycloakContainer keycloakContainer = new KeycloakContainer(
            "quay.io/keycloak/keycloak:23.0.6"
    )
            .withRealmImportFile("microservices-realm-realm.json")
            .waitingFor(Wait.forHttp("/health/started").forPort(8080))
            .withStartupTimeout(Duration.ofMinutes(5));

    @BeforeAll
    static void setup() {
        keycloakContainer.start();
    }

    @BeforeEach
    void setUp() {
        KeycloakProvider keycloakProvider = new KeycloakProvider();

        ReflectionTestUtils.setField(keycloakProvider, "serverUrl", keycloakContainer.getAuthServerUrl());
        ReflectionTestUtils.setField(keycloakProvider, "realm", "microservices-realm");
        ReflectionTestUtils.setField(keycloakProvider, "adminClientId", "admin-cli");
        ReflectionTestUtils.setField(keycloakProvider, "adminClientSecret", "MK6z3NJCRT9oTRqXXPvaKnQmm6D0UMbB");

        Keycloak keycloak = keycloakProvider.keycloak();

        userService = new UserServiceImpl(
                userRepository,
                keycloak
        );

        ReflectionTestUtils.setField(userService, "serverUrl", keycloakContainer.getAuthServerUrl());
        ReflectionTestUtils.setField(userService, "realm", "microservices-realm");
        ReflectionTestUtils.setField(userService, "clientID", "keycloak-client");
        ReflectionTestUtils.setField(userService, "clientSecret", "KdzCgauJ2QR1rRGUmAQsBOeMAyCi96kP");

    }

    @Order(1)
    @Test
    void testLogin_shouldReturnBadRequest() throws Exception {

        ServiceCustomException exception = assertThrows(ServiceCustomException.class, this::loginTestUser);

        assertEquals(400, exception.getStatus());
    }

    @Order(3)
    @Test
    void testLogin_shouldReturnOk() throws Exception {

        assertEquals(200, loginTestUser().getStatusCode().value());

    }

    ResponseEntity<?> loginTestUser() {
        LoginRequest loginRequest = LoginRequest.builder()
                .username("userTest1")
                .password("userTest1")
                .build();

       return userService.login(loginRequest.getUsername(), loginRequest.getPassword());

    }

    @Order(2)
    @Test
    void testRegisterUser_shouldReturnOk() throws Exception {

        CreateUserRequest userRequest = CreateUserRequest.builder()
                .username("userTest1")
                .email("userTest1@email.com")
                .password("userTest1")
                .firstName(null)
                .lastName(null)
                .build();


        ResponseEntity<?> response = userService.createKeycloakUser(userRequest);

        assertEquals(201, response.getStatusCode().value());
    }

}
