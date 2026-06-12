package com.example.inventoryservice.entity;

import com.example.inventoryservice.entity.enums.ReservationStatus;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Document(collection = "reservations")
@Setter
@Getter
@Builder
@AllArgsConstructor
public class InventoryReservation {
    @Id
    private UUID id;

    @Field("order_id")
    private UUID orderId;

    @Field("correlation_id")
    private UUID correlationId;

    @Field("reservation_items")
    private List<ReservationItem> items;

    private ReservationStatus status;

    @CreatedDate
    @Field("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private Instant updatedAt;
}
