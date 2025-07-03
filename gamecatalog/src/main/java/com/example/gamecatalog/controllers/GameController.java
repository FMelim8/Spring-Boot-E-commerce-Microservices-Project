package com.example.gamecatalog.controllers;

import com.example.gamecatalog.models.Game;
import com.example.gamecatalog.payload.response.GameDeletedResponse;
import com.example.gamecatalog.utils.SortField;
import com.example.gamecatalog.payload.request.GameCreateRequest;
import com.example.gamecatalog.payload.request.PaginationRequest;
import com.example.gamecatalog.payload.response.GameResponse;
import com.example.gamecatalog.payload.response.PagingResult;
import com.example.gamecatalog.service.GameService;
import com.example.sharedlib.annotations.LoggableController;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.info.BuildProperties;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@Validated
@OpenAPIDefinition(
        tags = {
                @Tag(name = "Visitor Access"),
                @Tag(name = "Admin Access"),
        }
)
@LoggableController
public class GameController {

    private final GameService gameService;
    private final BuildProperties buildProperties;

    @GetMapping("/")
    public ResponseEntity<?> root() {
        return new ResponseEntity<>("This is version " + buildProperties.getVersion() ,HttpStatus.OK);
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Add a Game. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully added a Game", content = @Content(mediaType = "application/json",
                    examples = {
                    @ExampleObject(value = """
                            {
                              "id": 31,
                              "title": "Ghost of Tsushima",
                              "description": "An open-world samurai action-adventure set in feudal Japan.",
                              "genre": "Action",
                              "type": "PS5",
                              "price": 59.99,
                              "stock": 100
                            }
                            """)
            })),
            @ApiResponse(responseCode = "400", description = "Bad Request, incorrect format", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:37:58.913142600Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Invalid request format",
                                      "path": "/api/games/create"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:07:56.883+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/games/create"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:08:47.712+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/games/create"
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
                              "path": "/api/games/create"
                            }
                            """)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/api/games/create")
    public ResponseEntity<?> addGame(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Add a game to the catalog", required = true,
            content = @Content(schema = @Schema(implementation = GameCreateRequest.class),
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "title": "Ghost of Tsushima",
                              "description": "An open-world samurai action-adventure set in feudal Japan.",
                              "genre": "Action",
                              "type": "PS5",
                              "price": 59.99,
                              "stock": 100,
                              "releaseDate": "2021-08-20"
                            }
                            """)
            )
    )

            @RequestBody GameCreateRequest gameRequest){

        GameResponse gameCreated = gameService.addGame(gameRequest);

        return new ResponseEntity<>(gameCreated, HttpStatus.CREATED);
    }

    @Tag(name = "Visitor Access")
    @Operation(operationId = "getGameById", summary = "Get a game by its Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful search", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "title": "Elden Ring",
                                      "description": "An open-world action RPG set in the Lands Between.",
                                      "genre": "Action",
                                      "type": "PS5",
                                      "price": 39.99,
                                      "stock": 88,
                                      "releaseDate": "2021-08-20"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "404", description = "No Game found with the provided Id", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:43:21.156306500Z",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Game not found with the id: 100",
                                      "path": "/api/games/100"
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
                              "path": "/api/games/100"
                            }
                            """)
            ))
    })
    @GetMapping("/api/games/{gameId}")
    public ResponseEntity<GameResponse> getGameById(@PathVariable long gameId) {

        GameResponse gameResponse = gameService.getGameById(gameId);

        return new ResponseEntity<>(gameResponse, HttpStatus.OK);
    }

    @Tag(name = "Visitor Access")
    @Operation(summary = "Get a list of all Games")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Search", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "title": "Elden Ring",
                                          "description": "An open-world action RPG set in the Lands Between.",
                                          "genre": "Action",
                                          "type": "PS5",
                                          "price": 39.99,
                                          "stock": 88,
                                          "releaseDate": "2021-08-20"
                                        },
                                        {
                                          "id": 2,
                                          "title": "God of War Ragnarok",
                                          "description": "Kratos and Atreus face Ragnarok in this Norse mythology epic.",
                                          "genre": "Action",
                                          "type": "PS5",
                                          "price": 69.99,
                                          "stock": 70,
                                          "releaseDate": "2022-11-09"
                                        }
                                      ],
                                      "totalPages": 14,
                                      "totalElements": 28,
                                      "size": 2,
                                      "page": 1,
                                      "empty": false
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
                              "path": "/api/games/all"
                            }
                            """)
            ))
    })
    @GetMapping("/api/games/all")
    public ResponseEntity<?> getAllGames(
            @Parameter(
                    description = "Page Number (Starts from 1) [Default: 1]"
            )
            @RequestParam(required = false) Integer page,

            @Parameter(
                    description = "Page size [Default: 10]"
            )
            @RequestParam(required = false) Integer size,

            @Parameter(
                    description = "Field to Sort By [Default: ID]"
            )
            @RequestParam(required = false) SortField sortField,

            @Parameter(
                    description = "Sort Direction [Default: ASC]"
            )
            @RequestParam(required = false) Sort.Direction direction
    ){

        final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
        PagingResult<GameResponse> games = gameService.getAllGames(request);

        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Reduce the stock quantity of a Game. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful stock reduction", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "title": "Elden Ring",
                                      "description": "An open-world action RPG set in the Lands Between.",
                                      "genre": "RPG",
                                      "type": "PC",
                                      "price": 59.99,
                                      "stock": 14
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "400", description = "Insufficient Stock", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:00:25.608396700Z",
                                      "status": 400,
                                      "error": "Insufficient Stock",
                                      "message": "Not enough stock",
                                      "path": "/api/games/reduceQuantity/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T15:57:28.754+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/games/reduceQuantity/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:01:28.000+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/games/reduceQuantity/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "404", description = "No Game found with the provided Id", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:03:27.737633100Z",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Game not found with the id: 100",
                                      "path": "/api/games/reduceQuantity/100"
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
                              "path": "/api/games/reduceQuantity/100"
                            }
                            """)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/games/reduceQuantity/{gameId}")
    public ResponseEntity<?> reduceQuantity(@PathVariable long gameId, @RequestParam long quantity){

        GameResponse updatedGame = gameService.reduceQuantity(gameId, quantity);

        return new ResponseEntity<>(updatedGame, HttpStatus.OK);
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Update the details of a Game. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the details of a Game", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 1,
                                      "title": "Elden Ring",
                                      "description": "An open-world action RPG set in the Lands Between.",
                                      "genre": "Action",
                                      "type": "PS5",
                                      "price": 39.99,
                                      "stock": 100,
                                      "releaseDate": "2021-08-20"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "400", description = "Invalid Request", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
																			"timestamp": "2025-06-23T15:46:07.861292034Z",
																			"status": 400,
																			"error": "Bad Request",
																			"message": "Invalid request format",
																			"path": "/api/games/update/1"
																		}
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T15:38:48.895+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/games/update/2"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T15:41:16.588+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/games/update/2"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "404", description = "No Game found with the provided Id", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T15:42:58.976844800Z",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Game not found with the id: 100",
                                      "path": "/api/games/update/100"
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
                              "path": "/api/games/update/100"
                            }
                            """)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/games/update/{gameId}")
    public ResponseEntity<?> updateGame(
            @PathVariable long gameId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Game update request",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = GameCreateRequest.class),
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Full Update", value = """
                                            {
                                                "title": "Ghost of Tsushima",
                                                "description": "An open-world samurai action-adventure set in feudal Japan.",
                                                "genre": "Action",
                                                "type": "PS5",
                                                "price": 59.99,
                                                "stock": 100,
                                                "releaseDate": "2021-08-20"
                                            }""",
                                            description = "Update all details of a Game"),
                                    @ExampleObject(name = "Partial Update", value = """
                                            {
                                                "title": "Ghost of Tsushima",
                                                "description": "An open-world samurai action-adventure set in feudal Japan."
                                            }""",
                                            description = "Update only the Name and Description"),
                                    @ExampleObject(name = "Price Update", value = """
                                            {
                                                "price": 39.99
                                            }""",
                                            description = "Update just the Price of a Game")
                            }
                    )
            )
            @Valid @RequestBody GameCreateRequest gameRequest
    )
    {
        GameResponse gameResponse = gameService.updateGameByGameId(gameId, gameRequest);

        return new ResponseEntity<>(gameResponse, HttpStatus.OK);
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Delete Game. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful deletion of a Game", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "id": 31
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                        @ExampleObject(value = """
                                {
                                  "timestamp": "2025-03-19T16:49:03.903+00:00",
                                  "status": 401,
                                  "error": "Unauthorized",
                                  "message": "Full authentication is required to access this resource",
                                  "path": "/api/games/14"
                                }
                                """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-19T16:52:47.051+00:00",
                                      "status": 403,
                                      "error": "Forbidden",
                                      "message": "Access Denied",
                                      "path": "/api/games/14"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "404", description = "No Game found with the provided Id", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject("""
                                    {
                                      "timestamp": "2025-03-19T16:53:53.110536500Z",
                                      "status": 404,
                                      "error": "Not Found",
                                      "message": "Game not found with the id: 100",
                                      "path": "/api/games/100"
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
                              "path": "/api/games/100"
                            }
                            """)
            ))
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/games/{gameId}")
    public ResponseEntity<?> deleteGameById(@PathVariable long gameId){

        GameDeletedResponse response = gameService.deleteGameById(gameId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Tag(name = "Visitor Access")
    @Operation(summary = "Obtain the details of multiple Games with the provided Ids")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrival of multiple Game details with the provided Ids", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Game.class))
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {{
                                      "timestamp": "2025-06-23T14:03:18.588646600Z",
                                      "status": 400,
                                      "error": "Bad Request",
                                      "message": "Required request parameter 'gameIds' for method parameter type List is not present",
                                      "path": "/api/games/batch"
                                    }
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/games/batch"
                            }
                            """)
            ))
    })
    @GetMapping("/api/games/batch")
    public ResponseEntity<?> getGamesByIds(
            @Parameter(
                    description = "List of game IDs to fetch"
            )
            @RequestParam List<Long> gameIds
    ) {
        List<GameResponse> response = gameService.getGamesByIds(gameIds);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}