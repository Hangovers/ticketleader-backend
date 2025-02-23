package com.ticketleader.api.dto.seat;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
@Introspected
public record LockContiguousSeatRequestDTO(
        @NotNull Long eventId,
        @NotNull Long sectorId,
        @NotNull Integer seatsPerRow,
        @NotNull Long reserveeId,
        @NotNull int requestedSeats) {}
