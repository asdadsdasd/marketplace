package com.example.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record OrderProductRequest(
        @NotNull
        UUID productId,

        @NotNull
        @NotBlank
        String productName,

        @NotNull
        @Min(1)
        Integer quantity,

        @NotNull
        BigDecimal price
) {
}
