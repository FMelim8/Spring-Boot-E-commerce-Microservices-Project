package com.example.gameorder.payloads.request;

import com.example.gameorder.utils.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {

    @NotBlank
    private long orderId;

    @NotBlank
    private BigDecimal amount;

    @NotBlank
    private PaymentMode paymentMode;
}
