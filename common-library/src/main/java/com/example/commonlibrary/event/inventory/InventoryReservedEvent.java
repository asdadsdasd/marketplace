package com.example.commonlibrary.event.inventory;

import com.example.commonlibrary.event.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
public class InventoryReservedEvent extends BaseEvent {
    private UUID orderId;

    public InventoryReservedEvent(UUID correlationId, UUID orderId) {
        super(correlationId);
        this.orderId = orderId;
    }
}
