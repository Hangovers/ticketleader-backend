package com.ticketleader.domain.event;

import com.ticketleader.domain.venue.Venue;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@MappedEntity("event")
public record Event(
        @Id @GeneratedValue @Nullable Long id,
        @NotBlank @MappedProperty("title") String title,
        @NotNull @MappedProperty("event_date") LocalDateTime eventDate,
        @NotNull @MappedProperty("capacity") Integer capacity,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @Nullable @MappedProperty("venue_id") Venue venue) {}
