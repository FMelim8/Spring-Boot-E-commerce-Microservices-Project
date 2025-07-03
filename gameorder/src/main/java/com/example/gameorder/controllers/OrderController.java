package com.example.gameorder.controllers;

import com.example.gameorder.utils.SortField;
import com.example.gameorder.payloads.request.OrderRequest;
import com.example.gameorder.payloads.request.PaginationRequest;
import com.example.gameorder.payloads.response.OrderCreateResponse;
import com.example.gameorder.payloads.response.OrderResponse;
import com.example.gameorder.service.OrderService;
import com.example.sharedlib.annotations.LoggableController;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Validated
@OpenAPIDefinition(
        tags = {
                @Tag(name = "User Access"),
                @Tag(name = "Admin Access")
        }
)
@LoggableController
public class OrderController {
    private final OrderService orderService;

    @Tag(name = "User Access")
    @Operation(summary = "Buy a Game with a provided Game Id and quantity. Access: 'USER'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Bought a Game successfully", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "id": 3855,
                              "gameId": 6,
                              "quantity": 7,
                              "status": "CREATED",
                              "totalAmount": 489.93
                            }
                            """)
            )),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T12:43:45.542809800Z",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "Invalid Request",
                              "path": "/api/order/placeorder"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T11:23:42.153+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/order/placeorder"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:45:36.885+00:00",
                              "status": 403,
                              "error": "Forbidden",
                              "message": "Access Denied",
                              "path": "/api/order/placeorder"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "404", description = "No Game found with the provided Game Id", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 404,
                              "error": "Not Found",
                              "message": "Game not found with the id: 100",
                              "path": "/api/order/placeorder"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 429,
                              "error": "Too Many Requests",
                              "message": "Service Temporarily Unavailable",
                              "path": "/api/order/placeorder"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/order/placeorder"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service GAME-CATALOG-SERVICE is unavailable",
                              "path": "/api/order/placeorder"
                            }
                            """)
            ))
    })
    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/placeorder")
    public ResponseEntity<?> placeOrder(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Order a Game", required = true,
            content = @Content(mediaType = "application/json",
            examples = @ExampleObject(value = """
                    {
                        "gameId" : 3,
                        "quantity" : 4,
                        "paymentMode" : "DEBIT_CARD"
                    }
                    """))
    )
            @RequestBody OrderRequest orderRequest) {

        OrderCreateResponse order = orderService.placeOrder(orderRequest);

        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @Tag(name = "Admin Access")
    @Tag(name = "User Access")
    @Operation(summary = "Get an order by its Id. If 'User' can only see own Orders. Access: 'ADMIN' / 'USER'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained and order by its Id successfully", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                    value = """
                            {
                              "orderId": 402,
                              "orderDate": "2025-03-12T16:59:06.123737Z",
                              "status": "PLACED",
                              "totalAmount": 179.97,
                              "gameDetails": {
                                "gameId": 7,
                                "title": "Hogwarts Legacy",
                                "price": 59.99,
                                "quantity": 3
                              },
                              "paymentDetails": {
                                "paymentId": 252,
                                "paymentMode": "CASH",
                                "paymentDate": "2025-03-12T16:59:06.742359Z",
                                "status": "SUCCESS"
                              },
                              "userDetails": {
                                "userId": 2,
                                "username": "user1",
                                "email": "user1@email.com"
                              }
                            }
                            """
            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(value = """
                                    {
                                      "timestamp": "2025-03-20T11:26:05.272+00:00",
                                      "status": 401,
                                      "error": "Unauthorized",
                                      "message": "Full authentication is required to access this resource",
                                      "path": "/api/order/1"
                                    }
                                    """)
                    }
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T12:45:04.747+00:00",
                              "status": 403,
                              "error": "Forbidden",
                              "message": "Access Denied",
                              "path": "/api/order/1"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "404", description = "No Order with the provided Id", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T12:50:39.340709300Z",
                              "status": 404,
                              "error": "Not Found",
                              "message": "Order not found with the id: 100",
                              "path": "/api/order/100"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-05-15T10:47:16.886776400Z",
                              "status": 429,
                              "error": "Too Many Requests",
                              "message": "Service Temporarily Unavailable",
                              "path": "/api/order/3553"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/order/100"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service GAME-CATALOG-SERVICE is unavailable",
                              "path": "/api/order/100"
                            }
                            """)
            ))
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {

        OrderResponse orderResponse = orderService.getOrderById(orderId);

        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @Tag(name = "Admin Access")
    @Operation(summary = "Get all Orders. Access: 'ADMIN'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Obtained successfully all Orders", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                    value = """
                            {
                              "content": [
                                {
                                  "orderId": 802,
                                  "orderDate": "2025-03-13T17:04:33.369682Z",
                                  "status": "PLACED",
                                  "totalAmount": 1399.8,
                                  "gameDetails": {
                                    "gameId": 28,
                                    "title": "Diablo IV",
                                    "price": 69.99,
                                    "quantity": 20
                                  },
                                  "paymentDetails": {
                                    "paymentId": 652,
                                    "paymentMode": "DEBIT_CARD",
                                    "paymentDate": "2025-03-13T17:04:35.541341Z",
                                    "status": "SUCCESS"
                                  },
                                  "userDetails": {
                                    "userId": 52,
                                    "username": "user4",
                                    "email": "user4@email.com"
                                  }
                                },
                                {
                                  "orderId": 902,
                                  "orderDate": "2025-03-14T15:11:16.920677Z",
                                  "status": "PLACED",
                                  "totalAmount": 79.98,
                                  "gameDetails": {
                                    "gameId": 5,
                                    "title": "Red Dead Redemption 2",
                                    "price": 39.99,
                                    "quantity": 2
                                  },
                                  "paymentDetails": {
                                    "paymentId": 752,
                                    "paymentMode": "DEBIT_CARD",
                                    "paymentDate": "2025-03-14T15:11:17.885450Z",
                                    "status": "SUCCESS"
                                  },
                                  "userDetails": {
                                    "userId": 52,
                                    "username": "user4",
                                    "email": "user4@email.com"
                                  }
                                }
                              ],
                              "totalPages": 3,
                              "totalElements": 6,
                              "size": 2,
                              "page": 1,
                              "empty": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:30:46.732+00:00",
                              "status": 401,
                              "error": "Unauthorized",
                              "message": "Full authentication is required to access this resource",
                              "path": "/api/order/all"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T12:46:12.928+00:00",
                              "status": 403,
                              "error": "Forbidden",
                              "message": "Access Denied",
                              "path": "/api/order/all"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-05-15T10:47:16.886776400Z",
                              "status": 429,
                              "error": "Too Many Requests",
                              "message": "Service Temporarily Unavailable",
                              "path": "/api/order/all"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/order/all"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service GAME-CATALOG-SERVICE is unavailable",
                              "path": "/api/order/all"
                            }
                            """)
            ))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(
            @Parameter(
                    description = "Page Number (Starts from 1) [Default: 1]"
            )
            @RequestParam(required = false) Integer page,

            @Parameter(
                    description = "Page size [Default: 10]"
            )
            @RequestParam(required = false) Integer size,

            @Parameter(
                    description = "Field to Sort By [Default: ORDERID]"
            )
            @RequestParam(required = false) SortField sortField,

            @Parameter(
                    description = "Sort Direction [Default: ASC]"
            )
            @RequestParam(required = false) Sort.Direction direction
    ){

        final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);

        return new ResponseEntity<>(orderService.getOrders(false, request), HttpStatus.OK);
    }

    @Tag(name = "User Access")
    @Operation(summary = "Obtain all Successful Orders of the current User. Access: 'USER'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully obtain current User's Orders", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(
                    value = """
                            {
                              "content": [
                                {
                                  "orderId": 2,
                                  "orderDate": "2025-06-18T13:49:01.465294Z",
                                  "status": "SUCCESS",
                                  "totalAmount": 279.96,
                                  "gameDetails": {
                                    "id": 3,
                                    "title": "The Legend of Zelda: Tears of the Kingdom",
                                    "price": 69.99
                                  },
                                  "paymentDetails": {
                                    "paymentId": 2,
                                    "paymentMode": "DEBIT_CARD",
                                    "paymentDate": "2025-06-18T13:49:01.469546Z",
                                    "status": "SUCCESS"
                                  },
                                  "userDetails": {
                                    "userId": 3,
                                    "username": "user2",
                                    "email": "user2@email.com"
                                  },
                                  "quantity": 4
                                }
                              ],
                              "totalPages": 1,
                              "totalElements": 1,
                              "size": 10,
                              "page": 1,
                              "empty": false
                            }
                            """
            ))),
            @ApiResponse(responseCode = "401", description = "Unauthorized Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:28:54.737+00:00",
                              "status": 401,
                              "error": "Unauthorized",
                              "message": "Full authentication is required to access this resource",
                              "path": "/api/order/currentUser"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "403", description = "Forbidden Access", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:45:36.885+00:00",
                              "status": 403,
                              "error": "Forbidden",
                              "message": "Access Denied",
                              "path": "/api/order/currentUser"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "429", description = "Too Many Requests", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-04-09T14:23:15.239281200Z",
                              "status": 429,
                              "error": "Too Many Requests",
                              "message": "Service Temporarily Unavailable",
                              "path": "/api/order/currentUser"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T11:49:18.422482600Z",
                              "status": 500,
                              "error": "Internal Serval Error",
                              "message": "message",
                              "path": "/api/order/currentUser"
                            }
                            """)
            )),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "timestamp": "2025-03-20T17:17:06.597301900Z",
                              "status": 503,
                              "error": "Service Unavailable",
                              "message": "Service GAME-CATALOG-SERVICE is unavailable",
                              "path": "/api/order/currentUser"
                            }
                            """)
            ))
    })
    @PreAuthorize("hasRole('USER') and !hasRole('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/currentUser")
    public ResponseEntity<?> getCurrentUserOrders(
            @Parameter(
                    description = "Page Number (Starts from 1) [Default: 1]"
            )
            @RequestParam(required = false) Integer page,

            @Parameter(
                    description = "Page size [Default: 10]"
            )
            @RequestParam(required = false) Integer size,

            @Parameter(
                    description = "Field to Sort By [Default: ORDERID]"
            )
            @RequestParam(required = false) SortField sortField,

            @Parameter(
                    description = "Sort Direction [Default: ASC]"
            )
            @RequestParam(required = false) Sort.Direction direction
    ){

        final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);

        return new ResponseEntity<>(orderService.getOrders(true, request), HttpStatus.OK);
    }
}
