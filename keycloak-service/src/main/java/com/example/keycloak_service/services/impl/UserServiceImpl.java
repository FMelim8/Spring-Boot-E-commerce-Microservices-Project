package com.example.keycloak_service.services.impl;

import com.example.keycloak_service.models.User;
import com.example.keycloak_service.payloads.request.CreateUserRequest;
import com.example.keycloak_service.payloads.response.CreateUserResponse;
import com.example.keycloak_service.payloads.response.LoginResponse;
import com.example.keycloak_service.repository.UserRepository;
import com.example.keycloak_service.services.UserService;
import com.example.sharedlib.exception.ServiceCustomException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${keycloak.auth-server-url}")
    public String serverUrl;

    @Value("${keycloak.realm}")
    public String realm;

    @Value("${keycloak.resource}")
    public String clientID;

    @Value("${keycloak.client.secret}")
    public String clientSecret;

    final private Keycloak keycloak;

    public ResponseEntity<?> logInKeycloakUser(String username, String password){
        try {
            AccessTokenResponse tokenResponse = KeycloakBuilder.builder()
                    .realm(realm)
                    .serverUrl(serverUrl)
                    .clientId(clientID)
                    .grantType(OAuth2Constants.PASSWORD)
                    .clientSecret(clientSecret)
                    .username(username)
                    .password(password)
                    .build()
                    .tokenManager().getAccessToken();

            LoginResponse loginResponse = LoginResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .tokenType(tokenResponse.getTokenType())
                    .expiresIn(tokenResponse.getExpiresIn())
                    .refreshExpiresIn(tokenResponse.getRefreshExpiresIn())
                    .sessionState(tokenResponse.getSessionState())
                    .scope(tokenResponse.getScope())
                    .build();

            return ResponseEntity.ok(loginResponse);
        } catch (NotAuthorizedException e) {
            throw new ServiceCustomException(
                    "Bad Request",
                    "Invalid Credentials",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }
        catch (Exception e) {
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
    }

    @Override
    @Transactional()
    public ResponseEntity<?> login(String username, String password) {

        ResponseEntity<?> response = logInKeycloakUser(username, password);

        String usernameKcId;
        try {
            usernameKcId = keycloak.realm(realm).users().searchByUsername(username, true).get(0).getId();
        } catch (Exception e) {
            return response;
        }

        if (!userInDB(usernameKcId)) {
            User user = User.builder()
                    .username(username)
                    .email(keycloak.realm(realm).users().searchByUsername(username, true).get(0).getEmail())
                    .subject(usernameKcId)
                    .build();

            userRepository.save(user);
        }
        return response;
    }

    @Override
    @Transactional()
    public ResponseEntity<?> createKeycloakUser(CreateUserRequest createUserRequest) {
        try {

            UserRepresentation user = new UserRepresentation();
            user.setEnabled(true);
            user.setUsername(createUserRequest.getUsername());
            user.setEmail(createUserRequest.getEmail());
            user.setFirstName(createUserRequest.getFirstName());
            user.setLastName(createUserRequest.getLastName());
            user.setEmailVerified(true);

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setValue(createUserRequest.getPassword());
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setTemporary(false);

            List<CredentialRepresentation> list = new ArrayList<>();
            list.add(credentialRepresentation);
            user.setCredentials(list);

            RealmResource realmResource = keycloak.realm(realm);
            UsersResource resource = realmResource.users();

            Response kcResponse = resource.create(user);

            String createdUserId = keycloak.realm(realm).users().searchByUsername(createUserRequest.getUsername(), true).get(0).getId();

            kcResponse.bufferEntity();


            if(Objects.equals(201, kcResponse.getStatus())){

                User dbUser = User.builder()
                        .email(createUserRequest.getEmail())
                        .username(createUserRequest.getUsername())
                        .subject(createdUserId)
                        .build();

                userRepository.save(dbUser);

                new CreateUserResponse();
                CreateUserResponse response = CreateUserResponse.builder()
                        .message("User Created")
                        .username(user.getUsername())
                        .build();

                return new ResponseEntity<>(response, HttpStatus.CREATED);
            }

        } catch (ProcessingException e) {

            throw new ServiceCustomException(
                    "Service Unavailable",
                    "Service temporarily unavailable",
                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                    ""
            );
        } catch (Exception e) {
            throw new ServiceCustomException(
                    "Bad Request",
                    "Invalid Request",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }
        throw new ServiceCustomException(
                "Bad Request",
                "Invalid Request",
                HttpStatus.BAD_REQUEST.value(),
                ""
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> getCurrentUserInfo(){
        try {
            String subFromJwt = getSubFromJwt();
            List<?> uList = userRepository.findUserBySubject(subFromJwt);
            Object response = uList.get(0);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<User> getUsersByIds(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);
        return users;
    }

    public String getSubFromJwt() {
        Object principal = SecurityContextHolder.getContext().getAuthentication();
        if (principal instanceof JwtAuthenticationToken jwtToken) {
            Jwt jwt = jwtToken.getToken();
            return jwt.getClaim("sub");
        }
        return null;
    }

    public Boolean userInDB(String id){
        return userRepository.existsUserBySubject(id);
    }
}
