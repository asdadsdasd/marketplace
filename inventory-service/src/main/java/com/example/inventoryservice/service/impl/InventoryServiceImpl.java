package com.example.inventoryservice.service.impl;

import com.example.commonlibrary.event.inventory.InventoryReservationFailedEvent;
import com.example.commonlibrary.event.inventory.InventoryReservedEvent;
import com.example.commonlibrary.event.order.OrderCreatedEvent;
import com.example.inventoryservice.entity.InventoryItem;
import com.example.inventoryservice.entity.InventoryReservation;
import com.example.inventoryservice.entity.ReservationItem;
import com.example.inventoryservice.entity.enums.ReservationStatus;
import com.example.inventoryservice.repository.InventoryRepository;
import com.example.inventoryservice.repository.ReservationRepository;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final InventoryRepository inventoryRepository;
    private final ReservationRepository reservationRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    @Transactional
    public void reserveInventory(OrderCreatedEvent orderCreatedEvent) {
        log.info("Processing inventory reservation for order: {}", orderCreatedEvent.getOrderId());

        List<ReservationItem> reservationItems = new ArrayList<>();
        boolean allItemsAvailable = true;
        StringBuilder insufficientItemsMessage = new StringBuilder();

        for (var orderItem : orderCreatedEvent.getItems()) {
            InventoryItem item = inventoryRepository.findById(orderItem.productId())
                    .orElse(null);

            if(item == null) {
                allItemsAvailable = false;
                insufficientItemsMessage.append("Product not found: ")
                        .append(orderItem.productId()).append("; ");
                continue;
            }

            if (item.getAvailableQuantity() < orderItem.quantity()) {
                allItemsAvailable = false;
                insufficientItemsMessage.append("Insufficient quantity for product: ")
                        .append(orderItem.productId())
                        .append(", requested: ").append(orderItem.quantity())
                        .append(", availability: ").append(item.getAvailableQuantity())
                        .append("; ");
                continue;
            }

            reservationItems.add(ReservationItem.builder()
                    .productId(orderItem.productId())
                    .quantity(orderItem.quantity())
                    .build());
        }

        if (!allItemsAvailable) {
            InventoryReservationFailedEvent failedEvent = new InventoryReservationFailedEvent(
                    orderCreatedEvent.getCorrelationId(),
                    orderCreatedEvent.getOrderId(),
                    insufficientItemsMessage.toString()
            );
            kafkaTemplate.send("inventory-events", failedEvent);
            log.error("Inventory reservation failed: {}", insufficientItemsMessage);
            return;
        }

        InventoryReservation reservation = InventoryReservation.builder()
                .id(UUID.randomUUID())
                .orderId(orderCreatedEvent.getOrderId())
                .correlationId(orderCreatedEvent.getCorrelationId())
                .items(reservationItems)
                .status(ReservationStatus.CONFIRMED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        reservationRepository.save(reservation);

        for (var reservationItem : reservationItems) {
            InventoryItem item = inventoryRepository.findById(reservationItem.getProductId()).orElseThrow();
            item.setAvailableQuantity(item.getAvailableQuantity() - reservationItem.getQuantity());
            item.setReservedQuantity(item.getReservedQuantity() + reservationItem.getQuantity());
            inventoryRepository.save(item);
        }

        InventoryReservedEvent reservedEvent = new InventoryReservedEvent(
                orderCreatedEvent.getCorrelationId(),
                orderCreatedEvent.getOrderId()
        );

        kafkaTemplate.send("inventory-events", reservedEvent);
        log.info("Inventory successfully reserved for order: {}", orderCreatedEvent.getOrderId());
    }

    @Override
    @Transactional
    public void confirmReservation(UUID orderId) {
        InventoryReservation reservation = reservationRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Reservation not found for order: " + orderId));

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(Instant.now());
        reservationRepository.save(reservation);
    }

    @Override
    public void cancelReservation(UUID orderId) {
        InventoryReservation reservation = reservationRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Reservation not found for order: " + orderId));

        if (reservation.getStatus() == ReservationStatus.CANCELED) {
            log.info("Reservation for order {} already cancelled", orderId);
            return;
        }

        for (var reservationItem : reservation.getItems()) {
            InventoryItem item = inventoryRepository.findById(reservationItem.getProductId()).orElseThrow();
            item.setAvailableQuantity(item.getReservedQuantity() + reservationItem.getQuantity());
            item.setReservedQuantity(item.getReservedQuantity() + reservationItem.getQuantity());
            inventoryRepository.save(item);
        }

        reservation.setStatus(ReservationStatus.CANCELED);
        reservation.setUpdatedAt(Instant.now());
        reservationRepository.save(reservation);

        log.info("Reservation cancelled and inventory released for order: {}", orderId);
    }
}
