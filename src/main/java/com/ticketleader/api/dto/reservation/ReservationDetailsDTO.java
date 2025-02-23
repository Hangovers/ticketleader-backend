package com.ticketleader.api.dto.reservation;

import com.ticketleader.domain.reservation.Reservation;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Serdeable
@Introspected
public record ReservationDetailsDTO(
        Long id,
        String eventName,
        LocalDateTime eventDate,
        String sectorName,
        BigDecimal price,
        @Nullable Integer seatNumber) {
    public static ReservationDetailsDTO from(Reservation reservation) {
        return new ReservationDetailsDTO(
                reservation.id(),
                reservation.event().title(),
                reservation.event().eventDate(),
                reservation.sector().name(),
                reservation.sector().price(),
                reservation.seat() == null ? null : reservation.seat().seatNumber());
    }
}
