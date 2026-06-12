package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryItemRequest;
import com.example.inventoryservice.dto.InventoryItemResponse;
import com.example.inventoryservice.entity.InventoryItem;
import com.example.inventoryservice.service.InventoryItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryItemService inventoryItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InventoryItemResponse> createInventoryItem(@RequestBody InventoryItemRequest request) {
        InventoryItem savedItem = inventoryItemService.createInventoryItem(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(savedItem));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItemResponse> getInventoryItemById(@PathVariable UUID id) {
        InventoryItem item = inventoryItemService.getInventoryItemById(id);
        return ResponseEntity.ok(mapToResponse(item));
    }

    @GetMapping
    public ResponseEntity<List<InventoryItemResponse>> getAllInventoryItems() {
        List<InventoryItem> items = inventoryItemService.getAllInventoryItems();
        List<InventoryItemResponse> responseItems = items.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventoryItemResponse> updateInventoryItem(
            @PathVariable UUID id,
            @RequestBody InventoryItemRequest request) {
        InventoryItem updatedItem = inventoryItemService.updateInventoryItem(id, request);
        return ResponseEntity.ok(mapToResponse(updatedItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventoryItem(@PathVariable UUID id) {
        inventoryItemService.deleteInventoryItem(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkInventoryAvailability(
            @RequestParam UUID productId,
            @RequestParam Integer quantity) {
        boolean isAvailable = inventoryItemService.checkAvailability(productId, quantity);
        return ResponseEntity.ok(isAvailable);
    }

    private InventoryItemResponse mapToResponse(InventoryItem item) {
        return InventoryItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .availableQuantity(item.getAvailableQuantity())
                .reservedQuantity(item.getReservedQuantity())
                .build();
    }
}
