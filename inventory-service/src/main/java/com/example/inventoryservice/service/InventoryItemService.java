package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryItemRequest;
import com.example.inventoryservice.entity.InventoryItem;

import java.util.List;
import java.util.UUID;

public interface InventoryItemService {
    InventoryItem createInventoryItem(InventoryItemRequest request);
    InventoryItem getInventoryItemById(UUID id);
    List<InventoryItem> getAllInventoryItems();
    InventoryItem updateInventoryItem(UUID id, InventoryItemRequest request);
    void deleteInventoryItem(UUID id);
    boolean checkAvailability(UUID productId, Integer quantity);
}
