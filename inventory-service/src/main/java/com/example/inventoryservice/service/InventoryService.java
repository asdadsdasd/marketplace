package com.example.inventoryservice.service;

import com.example.commonlibrary.event.order.OrderCreatedEvent;
import org.springframework.stereotype.Service;

import java.util.UUID;

public interface InventoryService {
    void reserveInventory(OrderCreatedEvent orderCreatedEvent);
    void confirmReservation(UUID orderId);
    void cancelReservation(UUID orderId);
}
