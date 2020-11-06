package com.commerce.backend.service;

import com.commerce.backend.model.request.order.PostOrderRequest;
import com.commerce.backend.model.response.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Integer getAllOrdersCount();

    List<OrderResponse> getAllOrders(Integer page, Integer pageSize);

    OrderResponse postOrder(PostOrderRequest postOrderRequest);
}
