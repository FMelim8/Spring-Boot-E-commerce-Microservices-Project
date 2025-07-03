package com.example.sharedlib.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameEvent {
    private Long orderId;
    private long gameId;
    private long quantity;
    private StockState stockState;
}
