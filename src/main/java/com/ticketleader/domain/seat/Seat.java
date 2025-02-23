package com.ticketleader.domain.seat;

import com.ticketleader.domain.sector.Sector;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotNull;

@MappedEntity("seat")
public record Seat(
        @Id @GeneratedValue @Nullable Long id,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @NotNull @MappedProperty("sector_id") Sector sector,
        @NotNull @MappedProperty("seat_number") Integer seatNumber) {}
