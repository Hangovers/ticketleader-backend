package com.ticketleader.api.dto.reservation;

import com.ticketleader.domain.reservation.Reservation;
import com.ticketleader.domain.sector.SectorType;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Serdeable
@Introspected
public record ReservationResponseDTO(
        Long id,
        String eventName,
        LocalDateTime eventDate,
        String sectorName,
        SectorType sectorType,
        BigDecimal price,
        String reserveeName,
        String reserveeLastName,
        Integer seatNumber) {
    public static ReservationResponseDTO from(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.id(),
                reservation.event().title(),
                reservation.event().eventDate(),
                reservation.sector().name(),
                reservation.sector().type(),
                reservation.sector().price(),
                reservation.reservee().firstName(),
                reservation.reservee().lastName(),
                reservation.seat().seatNumber());
    }
}
