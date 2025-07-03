package com.example.gameorder.payloads.response;

import com.example.gameorder.utils.PaymentMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderResponse {
    private Long orderId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]'Z'")
    private ZonedDateTime orderDate;

    private String status;
    private BigDecimal totalAmount;
    private GameDetails gameDetails;
    private PaymentDetails paymentDetails;
    private UserDetails userDetails;
    private long quantity;

    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class GameDetails {
        private Long id;
        private String title;
        private BigDecimal price;
    }

    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class PaymentDetails {
        private Long paymentId;
        private PaymentMode paymentMode;

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSSSSS]'Z'")
        private ZonedDateTime paymentDate;

        private String status;
    }

    @Data @Builder
    @AllArgsConstructor @NoArgsConstructor
    public static class UserDetails{
        private Long userId;
        private String username;
        private String email;
    }
}
