package com.example.gameorder.service.impl;

import com.example.gameorder.models.PaymentDetails;
import com.example.gameorder.payloads.request.PaymentRequest;
import com.example.gameorder.payloads.response.PaymentResponse;
import com.example.gameorder.repository.PaymentDetailsRepository;
import com.example.gameorder.service.PaymentService;
import com.example.gameorder.utils.PaymentMode;
import com.example.sharedlib.exception.ServiceCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static org.springframework.beans.BeanUtils.copyProperties;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public long executePayment(PaymentRequest paymentRequest, String status) {

        PaymentDetails paymentDetails = PaymentDetails.builder()
                .paymentDate(ZonedDateTime.now())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .status(status)
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .build();
        paymentDetailsRepository.save(paymentDetails);

        return paymentDetails.getId();
    }

    @Override
    public PaymentResponse getPaymentDetailsById(long orderId) {

        PaymentDetails paymentDetails = paymentDetailsRepository.findById(orderId).orElseThrow(
                () -> new ServiceCustomException(
                        "Not Found",
                        "Payment Details not found",
                        HttpStatus.NOT_FOUND.value(),
                        ""
                )
        );

        return PaymentResponse.builder()
                .paymentId(paymentDetails.getId())
                .paymentMode(PaymentMode.valueOf(paymentDetails.getPaymentMode()))
                .paymentDate(paymentDetails.getPaymentDate())
                .orderId(paymentDetails.getOrderId())
                .status(paymentDetails.getStatus())
                .amount(paymentDetails.getAmount())
                .build();
    }

    @Override
    public List<PaymentResponse> getAllPaymentDetails() {

        List<PaymentDetails> allPaymentDetails = paymentDetailsRepository.findAll();
        List<PaymentResponse> allPaymentResponses = new ArrayList<>();
        for (PaymentDetails paymentDetails : allPaymentDetails) {
            PaymentResponse paymentResponse = new PaymentResponse();
            copyProperties(paymentDetails, paymentResponse);
            allPaymentResponses.add(paymentResponse);
        }
        return allPaymentResponses;
    }
}
