package com.example.inventoryservice.repository;

import com.example.inventoryservice.entity.InventoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryRepository extends MongoRepository<InventoryItem, UUID> {
}
