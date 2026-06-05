package com.example.commonlibrary.event;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEvent {
    private UUID eventId;
    private UUID correlationId;
    private Instant timestamp;
    private Instant createdAt;

    public BaseEvent(UUID correlationId) {
        this.eventId = UUID.randomUUID();
        this.correlationId = correlationId;
        this.timestamp = Instant.now();
        this.createdAt = Instant.now();
    }
}
