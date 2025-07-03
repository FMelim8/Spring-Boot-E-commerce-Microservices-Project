package com.example.gameorder.service;

import com.example.gameorder.payloads.request.PaymentRequest;
import com.example.gameorder.payloads.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    long executePayment(PaymentRequest paymentRequest, String status);

    PaymentResponse getPaymentDetailsById(long orderId);

    List<PaymentResponse> getAllPaymentDetails();
}
