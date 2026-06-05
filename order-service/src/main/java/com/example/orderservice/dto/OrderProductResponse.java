package com.example.orderservice.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderProductResponse(
        UUID id,
        UUID productId,
        String productName,
        Integer quantity,
        BigDecimal price
) {
}
