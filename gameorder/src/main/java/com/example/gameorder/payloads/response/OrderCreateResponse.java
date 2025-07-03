package com.example.gameorder.payloads.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor
@NoArgsConstructor
public class OrderCreateResponse {
    private Long id;

    private long gameId;

    private long quantity;

    private String status;

    private BigDecimal totalAmount;
}
