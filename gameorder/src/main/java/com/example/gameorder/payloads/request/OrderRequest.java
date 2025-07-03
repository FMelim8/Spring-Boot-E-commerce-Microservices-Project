package com.example.gameorder.payloads.request;

import com.example.gameorder.utils.PaymentMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class OrderRequest {
    @NotBlank
    private Long gameId;

    @NotBlank
    private long quantity;

    @NotBlank
    private PaymentMode paymentMode;
}
