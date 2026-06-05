package com.example.commonlibrary.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderProductDto(
        UUID productId,
        Integer quantity,
        BigDecimal price
) {}
