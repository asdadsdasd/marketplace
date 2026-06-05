package com.example.orderservice.entity.enums;

public enum OrderStatus {
    PENDING,
    INVENTORY_CHECKING,
    INVENTORY_RESERVED,
    PAYMENT_PROCESSING,
    PAYMENT_COMPLETED,
    SHIPPING_PROCESSING,
    SHIPPED,
    COMPLETED,
    CANCELLED,
    FAILED
}