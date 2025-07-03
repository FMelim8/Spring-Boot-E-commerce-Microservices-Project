package com.example.sharedlib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private long gameId;
    private long quantity;
}
