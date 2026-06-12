package com.example.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InventoryItemRequest(
        @NotBlank
        String name,

        String description,

        @NotNull
        @PositiveOrZero
        Integer quantity
) {
}
