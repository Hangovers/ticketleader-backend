package com.ticketleader.api.dto.seat;

import com.ticketleader.domain.seat.Seat;
import com.ticketleader.domain.sector.Sector;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotNull;

@Serdeable
@Introspected
public record SeatDTO(@Nullable Long id, @NotNull Sector sector, @NotNull Integer seatNumber) {
    public Seat toEntity() {
        return new Seat(id, sector, seatNumber);
    }

    public static SeatDTO fromEntity(Seat seat) {
        return new SeatDTO(seat.id(), seat.sector(), seat.seatNumber());
    }
}
