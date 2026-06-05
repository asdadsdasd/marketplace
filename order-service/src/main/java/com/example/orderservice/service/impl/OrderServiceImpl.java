package com.example.orderservice.service.impl;

import com.example.commonlibrary.dto.OrderProductDto;
import com.example.commonlibrary.event.order.OrderCreatedEvent;
import com.example.orderservice.dto.OrderProductResponse;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderProduct;
import com.example.orderservice.entity.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderResponse createOrder(OrderRequest request) {
        List<OrderProduct> orderItems = request.items().stream()
                .map(item -> OrderProduct.builder()
                        .productId(item.productId())
                        .productName(item.productName())
                        .quantity(item.quantity())
                        .price(item.price())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(request.userId())
                .totalAmount(totalAmount)
                .status(OrderStatus.INVENTORY_CHECKING)
                .items(orderItems)
                .build();

        Order savedOrder = orderRepository.save(order);

        UUID correlationId = UUID.randomUUID();
        List<OrderProductDto> itemsDto = savedOrder.getItems().stream()
                .map(item -> new OrderProductDto(
                        item.getProductId(),
                        item.getQuantity(),
                        item.getPrice()))
                .toList();

        OrderCreatedEvent event = new OrderCreatedEvent(
                correlationId,
                savedOrder.getId(),
                savedOrder.getUserId(),
                itemsDto,
                savedOrder.getTotalAmount()
        );

        kafkaTemplate.send("order-events", event);

        return mapToOrderResponse(savedOrder);
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return mapToOrderResponse(order);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(UUID userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrderStatus(UUID orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));

        order.setStatus(status);
        orderRepository.save(order);
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderProductResponse> itemResponses = order.getItems().stream()
                .map(item -> OrderProductResponse.builder()
                        .id(item.getId())
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .toList();

        return OrderResponse.builder()
                .userId(order.getUserId())
                .orderId(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .items(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
