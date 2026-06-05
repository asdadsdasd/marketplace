package com.example.orderservice.dto;

import com.example.orderservice.entity.enums.OrderStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record OrderResponse(
        UUID orderId,
        UUID userId,
        BigDecimal totalAmount,
        OrderStatus status,
        List<OrderProductResponse> items,
        Instant createdAt,
        Instant updatedAt
) {
}
