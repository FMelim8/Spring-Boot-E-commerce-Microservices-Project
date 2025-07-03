package com.example.gameorder.payloads.response;

import com.example.gameorder.utils.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class PaymentResponse {
    private long paymentId;
    private long orderId;
    private String status;
    private BigDecimal amount;
    private ZonedDateTime paymentDate;
    private PaymentMode paymentMode;
}
