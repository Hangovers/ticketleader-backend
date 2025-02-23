package com.ticketleader.api.dto.seat;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
@Introspected
public record SeatLockRequestDTO(Long eventId, Long sectorId, Long reserveeId, List<Long> seatIds) {}
