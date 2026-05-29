package com.example.orderservice.service;

import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    public Long createOrder(
            Long userId,
            CreateOrderRequest request
    ) {

        Order order = new Order();

        order.setUserId(userId);
        order.setTotalPrice(request.totalPrice());
        order.setStatus(OrderStatus.CREATED);

        return orderRepository
                .save(order)
                .getId();
    }
}
