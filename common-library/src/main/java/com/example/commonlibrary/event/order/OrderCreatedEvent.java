package com.example.commonlibrary.event.order;

import com.example.commonlibrary.dto.OrderProductDto;
import com.example.commonlibrary.event.BaseEvent;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class OrderCreatedEvent extends BaseEvent {
    private UUID orderId;
    private UUID customerId;
    private List<OrderProductDto> items;
    private BigDecimal totalAmount;

    public OrderCreatedEvent(UUID correlationId, UUID orderId, UUID customerId,
                             List<OrderProductDto> items, BigDecimal totalAmount) {
        super(correlationId);
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}
