package com.ticketleader.api.dto.reservation;

import com.ticketleader.api.dto.event.EventDTO;
import com.ticketleader.api.dto.reservee.ReserveeDTO;
import com.ticketleader.api.dto.seat.SeatDTO;
import com.ticketleader.api.dto.sector.SectorDTO;
import com.ticketleader.domain.reservation.Reservation;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Serdeable
@Introspected
public record ReservationDTO(
        @Nullable Long id,
        @NotNull EventDTO event,
        @NotNull SectorDTO sector,
        @NotNull ReserveeDTO reservee,
        @NotNull SeatDTO seat) {
    public Reservation toEntity() {
        return new Reservation(
                id(), event.toEntity(), sector.toEntity(), reservee.toEntity(), seat.toEntity(), LocalDateTime.now());
    }
}
