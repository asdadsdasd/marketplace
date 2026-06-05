package com.example.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record OrderRequest(
        @NotNull
        UUID userId,

        @NotEmpty
        List<OrderProductRequest> items
) { }
