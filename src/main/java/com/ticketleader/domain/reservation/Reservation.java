package com.ticketleader.domain.reservation;

import com.ticketleader.domain.event.Event;
import com.ticketleader.domain.reservee.Reservee;
import com.ticketleader.domain.seat.Seat;
import com.ticketleader.domain.sector.Sector;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedEntity("reservation")
public record Reservation(
        @Id @GeneratedValue @Nullable Long id,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @NotNull @MappedProperty("event_id") Event event,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @NotNull @MappedProperty("sector_id") Sector sector,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @MappedProperty("reservee_id") Reservee reservee,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @Nullable @MappedProperty("seat_id") Seat seat,
        @NotNull @MappedProperty("created_at") LocalDateTime createdAt) {}
