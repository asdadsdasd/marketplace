package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryReservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends MongoRepository<InventoryReservation, UUID> {
    Optional<InventoryReservation> findByOrderId(UUID orderId);
}
