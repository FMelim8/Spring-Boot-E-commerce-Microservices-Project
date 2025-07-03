package com.example.gameorder.service.impl;

import com.example.gameorder.external.client.GameServiceClient;
import com.example.gameorder.external.client.KeycloakUserServiceClient;
import com.example.gameorder.kafka.KafkaPublisher;
import com.example.gameorder.models.OrderDetails;
import com.example.gameorder.models.PaymentDetails;
import com.example.gameorder.payloads.request.OrderRequest;
import com.example.gameorder.payloads.request.PaginationRequest;
import com.example.gameorder.payloads.request.PaymentRequest;
import com.example.gameorder.payloads.request.UserRequest;
import com.example.gameorder.payloads.response.OrderCreateResponse;
import com.example.gameorder.payloads.response.OrderResponse;
import com.example.gameorder.payloads.response.PagingResult;
import com.example.gameorder.payloads.response.PaymentResponse;
import com.example.gameorder.repository.OrderDetailsRepository;
import com.example.gameorder.repository.PaymentDetailsRepository;
import com.example.gameorder.service.AuthService;
import com.example.gameorder.service.OrderService;
import com.example.gameorder.service.PaymentService;
import com.example.gameorder.utils.PaymentMode;
import com.example.gameorder.utils.PaginationUtils;
import com.example.sharedlib.dto.GameEvent;
import com.example.sharedlib.dto.OrderEvent;
import com.example.sharedlib.dto.StockState;
import com.example.sharedlib.exception.ServiceCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Log4j2
@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class OrderServiceImpl implements OrderService {

    private final OrderDetailsRepository orderRepository;
    private final KafkaPublisher kafkaPublisher;
    private final PaymentDetailsRepository paymentDetailsRepository;
    private final GameServiceClient gameServiceClient;
    private final KeycloakUserServiceClient keycloakUserServiceClient;
    private final PaymentService paymentService;
    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void updateOrder(GameEvent gameEvent) {
        System.out.println("==========UPDATE ORDER TRIGGERED==========");
        Optional<OrderDetails> newOrder = orderRepository.findById(gameEvent.getOrderId());
        if (newOrder.isPresent() && Objects.equals(newOrder.get().getPayment().getStatus(), "SUCCESS")) {
            OrderDetails orderDetails = newOrder.get();
            switch (gameEvent.getStockState()) {
                case AVAILABLE -> {
                    logger.info("Order Succeeded",
                            kv("Service", "Gameorder"),
                            kv("Operation", "updateOrder")
                    );

                    orderDetails.setStatus("SUCCESS");
                }
                case OUT_OF_STOCK -> {
                    logger.info("Order Failed due to lack of stock",
                            kv("Cause", StockState.OUT_OF_STOCK.name()),
                            kv("Service", "Gameorder"),
                            kv("Operation", "updateOrder")
                    );

                    orderDetails.setStatus("FAILED");
                    orderDetails.getPayment().setStatus("REIMBURSED");
                }
                case NOT_AVAILABLE -> {
                    logger.info("Order Failed due to lack of availability",
                            kv("Cause", StockState.NOT_AVAILABLE.name()),
                            kv("Service", "Gameorder"),
                            kv("Operation", "updateOrder")
                    );

                    orderDetails.setStatus("FAILED");
                    orderDetails.getPayment().setStatus("REIMBURSED");
                }
            }
            orderRepository.save(orderDetails);
        }
        else{
            logger.info("Order Failed due to payment failure",
                    kv("Cause", "PAYMENT_FAILED"),
                    kv("Service", "Gameorder"),
                    kv("Operation", "updateOrder")
            );
            try {
                assert newOrder.isPresent() : "Order Not Found";

            } catch (AssertionError e) {
                logger.warn(e.getMessage(),
                        kv("Service", "Gameorder"),
                        kv("Operation", "updateOrder"));
            }

        }
    }

    @Override
    @Transactional()
    public OrderCreateResponse placeOrder(OrderRequest orderRequest) {

        UserRequest currentUserInfo = getCurrentUserInfo();

        if (currentUserInfo.getSubject() == null || currentUserInfo.getSubject().isEmpty()) {
            logger.error("Error getting subject",
                    kv("Error", "User request: " + currentUserInfo));
            throw new ServiceCustomException(
                    "Service Unavailable",
                    "Service Temporarily Unavailable",
                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                    ""
            );
        }

        if(orderRequest.getQuantity() <= 0){
            throw new ServiceCustomException(
                    "Bad Request",
                    "Invalid Quantity",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }

        OrderResponse.GameDetails gameTarget = getGameById(orderRequest.getGameId());

        OrderDetails orderDetails = OrderDetails.builder()
                .totalAmount(gameTarget.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                .status("CREATED")
                .gameId(orderRequest.getGameId())
                .userId(Objects.requireNonNull(currentUserInfo).getId())
                .orderDate(ZonedDateTime.now())
                .quantity(orderRequest.getQuantity()).build();

        if (orderRequest.getPaymentMode() == null){

            throw new ServiceCustomException(
                    "Bad Request",
                    "No payment method provided",
                    HttpStatus.BAD_REQUEST.value(),
                    ""
            );
        }
        orderRepository.save(orderDetails);

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(orderDetails.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(gameTarget.getPrice().multiply(BigDecimal.valueOf(orderRequest.getQuantity())))
                .build();

        paymentService.executePayment(paymentRequest, "SUCCESS" );

        OrderEvent orderEvent = new OrderEvent(orderDetails.getId(), orderDetails.getGameId(), orderDetails.getQuantity());
        kafkaPublisher.publish("orderEventSupplier-out-0", orderEvent);

        return new OrderCreateResponse(
                orderDetails.getId(),
                orderDetails.getGameId(),
                orderDetails.getQuantity(),
                orderDetails.getStatus(),
                orderDetails.getTotalAmount()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(long orderId) {

        OrderDetails orderDetails = orderRepository.findById(orderId).orElseThrow(
                () -> new ServiceCustomException(
                        "Not Found",
                        "Order not found with the id: " + orderId,
                        HttpStatus.NOT_FOUND.value(),
                        ""
                )
        );

        UserRequest userRequest = getCurrentUserInfo();
        boolean isAdmin = authService.isAdmin();

        if (!isAdmin && (orderDetails.getUserId() != userRequest.getId())){
            throw new AccessDeniedException("Access Denied");
        } else if (isAdmin) {
            userRequest = getUserInfoById(orderDetails.getUserId());
        }

        OrderResponse.GameDetails gameResponse = getGameById(orderDetails.getGameId());

        PaymentDetails paymentD = paymentDetailsRepository.findByOrderId(orderId).orElseThrow(
                () -> new ServiceCustomException(
                        "Not Found",
                        "Payment Details not found",
                        HttpStatus.NOT_FOUND.value(),
                        ""
                )
        );

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .paymentId(paymentD.getId())
                .paymentMode(PaymentMode.valueOf(paymentD.getPaymentMode()))
                .paymentDate(paymentD.getPaymentDate())
                .orderId(paymentD.getOrderId())
                .status(paymentD.getStatus())
                .amount(paymentD.getAmount())
                .build();

        assert gameResponse != null;
        OrderResponse.GameDetails gameDetails = OrderResponse.GameDetails.builder()
                .title(gameResponse.getTitle())
                .id(orderDetails.getGameId())
                .price(gameResponse.getPrice())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .status(paymentResponse.getStatus())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        assert userRequest != null;
        OrderResponse.UserDetails userDetails = OrderResponse.UserDetails.builder()
                .username(userRequest.getUsername())
                .userId(userRequest.getId())
                .email(userRequest.getEmail())
                .build();

        return OrderResponse.builder()
                .orderId(orderDetails.getId())
                .status(orderDetails.getStatus())
                .quantity(orderDetails.getQuantity())
                .totalAmount(orderDetails.getTotalAmount())
                .orderDate(orderDetails.getOrderDate())
                .gameDetails(gameDetails)
                .paymentDetails(paymentDetails)
                .userDetails(userDetails)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PagingResult<OrderResponse> getOrders(Boolean userRestricted, PaginationRequest request) {

        final Pageable pageable = PaginationUtils.getPageable(request);
        UserRequest currentUser = userRestricted ? getCurrentUserInfo() : null;

        // Fetch orders
        Page<OrderDetails> orderList = userRestricted
                ? orderRepository.findAllByUserId(currentUser.getId(), pageable)
                : orderRepository.findAll(pageable);

        List<Long> orderIds = orderList.stream().map(OrderDetails::getId).toList();
        List<Long> gameIds = orderList.stream().map(OrderDetails::getGameId).toList();
        List<Long> userIds = orderList.stream().map(OrderDetails::getUserId).toList();

        // Fetch payments
        Map<Long, PaymentDetails> paymentMap = paymentDetailsRepository.findAllByOrderIdIn(orderIds)
                .stream().collect(Collectors.toMap(PaymentDetails::getOrderId, p -> p));

        // Fetch users if unrestricted
        Map<Long, UserRequest> userMap;
        if (!userRestricted) {
            userMap = getUsersByIds(userIds)
                    .stream().collect(Collectors.toMap(UserRequest::getId, u -> u));
        } else {
            userMap = null;
        }

        // Fetch games
        Map<Long, OrderResponse.GameDetails> gameMap = getGamesByIds(gameIds)
                .stream().collect(Collectors.toMap(OrderResponse.GameDetails::getId, g -> g));

        List<OrderResponse> orderResponseList = orderList.stream().map(orderDetails -> {
            PaymentDetails paymentD = paymentMap.get(orderDetails.getId());
            if (paymentD == null) {
                throw new ServiceCustomException("Not Found", "Payment Details not found",
                        HttpStatus.NOT_FOUND.value(), "");
            }

            UserRequest userRequest = (userMap != null) ? userMap.get(orderDetails.getUserId()) : currentUser;
            assert userRequest != null;

            OrderResponse.UserDetails userDetails = OrderResponse.UserDetails.builder()
                    .username(userRequest.getUsername())
                    .userId(userRequest.getId())
                    .email(userRequest.getEmail())
                    .build();

            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .paymentId(paymentD.getId())
                    .paymentMode(PaymentMode.valueOf(paymentD.getPaymentMode()))
                    .paymentDate(paymentD.getPaymentDate())
                    .orderId(paymentD.getOrderId())
                    .status(paymentD.getStatus())
                    .amount(paymentD.getAmount())
                    .build();

            OrderResponse.GameDetails gameDetails = gameMap.get(orderDetails.getGameId());

            OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                    .paymentId(paymentResponse.getPaymentId())
                    .status(paymentResponse.getStatus())
                    .paymentDate(paymentResponse.getPaymentDate())
                    .paymentMode(paymentResponse.getPaymentMode())
                    .build();

            return OrderResponse.builder()
                    .orderId(orderDetails.getId())
                    .quantity(orderDetails.getQuantity())
                    .status(orderDetails.getStatus())
                    .totalAmount(orderDetails.getTotalAmount())
                    .orderDate(orderDetails.getOrderDate())
                    .gameDetails(gameDetails)
                    .paymentDetails(paymentDetails)
                    .userDetails(userDetails)
                    .build();
        }).toList();

        return new PagingResult<>(orderResponseList, orderList.getTotalPages(),
                orderList.getTotalElements(), orderList.getSize(), orderList.getNumber(),
                orderList.isEmpty());

    }

    @Transactional(readOnly = true)
    public OrderResponse.GameDetails getGameById(Long gameId) {

        try {
            OrderResponse.GameDetails response = gameServiceClient.getGameById(gameId).getBody();

            assert response != null;
            response.setId(gameId);
            return response;
        } catch (Exception e) {
            logger.error("Error while fetching game details",
                    kv("Error",e)
            );
            throw new ServiceCustomException(
                    "Service Unavailable",
                    "Service Temporarily Unavailable",
                    HttpStatus.SERVICE_UNAVAILABLE.value(),
                    ""
            );
        }
    }

    @Transactional(readOnly = true)
    public UserRequest getCurrentUserInfo(){
        ResponseEntity<UserRequest> response = keycloakUserServiceClient.getCurrentUserInfo();
        return (UserRequest) validateResponse(response,"getCurrentUserInfo","Keycloak-Service").getBody();
    }

    @Transactional(readOnly = true)
    public UserRequest getUserInfoById(long userId){
        ResponseEntity<UserRequest> response = keycloakUserServiceClient.getUserInfoById(userId);
        return (UserRequest) validateResponse(response,"getUserInfoById","Keycloak-Service").getBody();
    }

    @Transactional(readOnly = true)
    public List<OrderResponse.GameDetails> getGamesByIds(List<Long> userIds){
        ResponseEntity<List<OrderResponse.GameDetails>> response = gameServiceClient.getGamesByIds(userIds);
        return (List<OrderResponse.GameDetails>) validateResponse(response, "getGamesByIds","Gamecatalog-Service").getBody();
    }

    @Transactional(readOnly = true)
    public List<UserRequest> getUsersByIds(List<Long> userIds){
        ResponseEntity<List<UserRequest>> response = keycloakUserServiceClient.getUsersByIds(userIds);
        return (List<UserRequest>) validateResponse(response,"getUsersByIds","Keycloak-Service").getBody();
    }

    public ResponseEntity<?> validateResponse(ResponseEntity<?> response, String operation, String origin) {
        return switch (response.getStatusCode().value()) {
            case 503 -> {
                logger.warn("Fallback Triggered",
                        kv("Service", "Gameorder"),
                        kv("Operation", operation),
                        kv("Origin", origin),
                        kv("Response",response.getBody())
                        );

                throw new ServiceCustomException(
                "Service Unavailable",
                "Service Temporarily Unavailable",
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                ""
        );
            }
            case 429 -> {

                logger.warn("Bulkhead Triggered",
                        kv("Service", "Gameorder"),
                        kv("Operation", operation),
                        kv("Origin", origin),
                        kv("Response",response.getBody())
                );

                throw new ServiceCustomException(
                "Too Many Requests",
                "Service Temporarily Unavailable",
                HttpStatus.TOO_MANY_REQUESTS.value(),
                ""
        );
            }
            default -> response;
        };
    }

}
