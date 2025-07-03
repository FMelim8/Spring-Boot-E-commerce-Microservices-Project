package com.example.keycloak_service.controllers;

import com.example.keycloak_service.models.User;
import com.example.keycloak_service.payloads.request.CreateUserRequest;
import com.example.keycloak_service.payloads.request.LoginRequest;
import com.example.keycloak_service.payloads.response.LoginResponse;
import com.example.keycloak_service.repository.UserRepository;
import com.example.keycloak_service.services.UserService;
import com.example.sharedlib.annotations.LoggableController;
import com.example.sharedlib.exception.ServiceCustomException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
@OpenAPIDefinition(
        tags = {
                @Tag(name = "Login / Register"),
                @Tag(name = "User Access"),
                @Tag(name = "Admin Access")
        }
)
@LoggableController
public class AuthController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Tag(name = "Login / Register")
    @Operation(summary = "Obtain the Bearer token of a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful User login",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid Details", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T17:54:09.534161300Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Invalid Credentials",
                                      "path": "/api/login"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/login"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service temporarily unavailable",
                              "path": "/api/login"
                            }
                            """)
            ))
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User to perform Login", required = true,
            content = @Content(schema = @Schema(implementation = LoginRequest.class),
                mediaType = "application/json",
                examples = {
                        @ExampleObject(name="Default admin login", value = "{ \"username\" : \"user1\", \"password\" : \"user1\"}"),
                        @ExampleObject(name="Default user login", value = "{ \"username\" : \"user2\", \"password\" : \"user2\"}")
                })
    )
            @RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Tag(name = "Login / Register")
    @Operation(summary = "Register User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful User registration",
            content = @Content(mediaType = "application/json",
                    examples = {
                        @ExampleObject(value = """
                                {
                                  "message": "User Created",
                                  "username": "userTeste"
                                }
                                """)
                    }
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = "application/json",
                    examples = {
                        @ExampleObject(value = """
                                {
                                  "timestamp": "2025-03-20T10:06:45.854780900Z",
                                  "status": 400,
                                  "error": "Bad Request",
                                  "message": "Invalid Request",
                                  "path": "/api/register"
                                }
                                """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/register"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service temporarily unavailable",
                              "path": "/api/register"
                            }
                            """)
            ))
    })
    @Produces(MediaType.APPLICATION_JSON)
    @PostMapping("/register")
    public ResponseEntity<?> register(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User to perform Registration", required = true,
            content = @Content(schema = @Schema(implementation = CreateUserRequest.class),
                mediaType = "application/json",
                examples = {@ExampleObject(name = "Example 1", summary = "Register user with first and last name",
                                value = """
                                        {
                                          "email": "userTeste@email.com",
                                          "username": "userTeste",
                                          "password": "userTeste",
                                          "firstName": "John",
                                          "lastName": "Doe"
                                        }
                                        """),
                            @ExampleObject(name = "Example 2", summary = "Register user without first and last name",
                                value = """
                                        {
                                          "email": "userTeste@email.com",
                                          "username": "userTeste",
                                          "password": "userTeste"
                                        }
                                        """)})
    )
            @Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.createKeycloakUser(createUserRequest);
    }

    @Tag(name = "User Access")
    @Tag(name = "Admin Access")
    @Operation(summary = "Obtain the details of the current user from their JWT. Access: 'USER' / 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrival of current user details", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:39:17.514+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/userDetails"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/userDetails"
                            }
                            """)
            ))
    })
    @GetMapping("/userDetails")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserDetails(){
        return userService.getCurrentUserInfo();
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Obtain the details of a User with a provided Id. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrival of User details with the provided Id", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = User.class)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:39:17.514+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/userDetails/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:45:48.158+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/userDetails/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "404", description = "No user found with the provided Id", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:53:40.922848500Z",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "User with Id: 100 not found",
                                      "path": "/api/userDetails/100"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/userDetails/100"
                            }
                            """)
            ))
    })
    @GetMapping("/userDetails/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserDetailById(@PathVariable long id) {
        if (userRepository.existsById(id)) {
            return new ResponseEntity<>(userRepository.findById(id), HttpStatus.OK);
        }

        throw new ServiceCustomException(
                "Not Found",
                "User with Id: " + id + " not found",
                HttpStatus.NOT_FOUND.value(),
                ""
        );
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Obtain the details of multiple Users with the provided Ids. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrival of multiple User details with the provided Ids", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = User.class))
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-06-23T14:03:18.588646600Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Required request parameter 'userIds' for method parameter type List is not present",
                                      "path": "/api/userDetails/batch"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:39:17.514+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/userDetails/batch"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T10:45:48.158+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/userDetails/batch"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/userDetails/batch"
                            }
                            """)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/userDetails/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsersByIds(
            @Parameter(
                    description = "List of user IDs to fetch"
            )
            @RequestParam List<Long> userIds
    ) {
        return userService.getUsersByIds(userIds);
    }
}
