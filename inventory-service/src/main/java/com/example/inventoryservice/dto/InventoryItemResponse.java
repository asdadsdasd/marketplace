package com.example.inventoryservice.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record InventoryItemResponse(
      UUID id,
      String name,
      String description,
      Integer availableQuantity,
      Integer reservedQuantity
) {
}
