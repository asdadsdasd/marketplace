package com.example.inventoryservice.entity;

import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationItem {
    private UUID productId;
    private Integer quantity;
}
