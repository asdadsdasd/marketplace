package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.enums.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrderById(UUID orderId);
    List<OrderResponse> getAllOrders();
    List<OrderResponse> getOrdersByUserId(UUID userId);
    void updateOrderStatus(UUID orderId, OrderStatus status);
}
