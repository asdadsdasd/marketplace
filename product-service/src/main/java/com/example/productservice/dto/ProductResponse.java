package com.example.productservice.dto;

import com.example.productservice.entity.Product;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        Long stock
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getPrice(),
                product.getStock());
    }
}
