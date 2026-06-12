package com.example.commonlibrary.event.inventory;

import com.example.commonlibrary.event.BaseEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class InventoryReservationFailedEvent extends BaseEvent {
    private UUID orderId;
    private String reason;

    public InventoryReservationFailedEvent(UUID correlationId, UUID orderId, String reason) {
        super(correlationId);
        this.orderId = orderId;
        this.reason = reason;
    }
}
