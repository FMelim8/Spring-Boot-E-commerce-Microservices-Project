package com.example.gameorder.service;

import com.example.gameorder.payloads.request.OrderRequest;
import com.example.gameorder.payloads.request.PaginationRequest;
import com.example.gameorder.payloads.response.OrderCreateResponse;
import com.example.gameorder.payloads.response.OrderResponse;
import com.example.gameorder.payloads.response.PagingResult;
import com.example.sharedlib.dto.GameEvent;

public interface OrderService {
    OrderCreateResponse placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(long orderId);

    PagingResult<OrderResponse> getOrders(Boolean userRestricted, PaginationRequest request);

    void updateOrder(GameEvent gameEvent);

}
