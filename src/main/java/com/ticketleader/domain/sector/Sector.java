package com.ticketleader.domain.sector;

import com.ticketleader.domain.venue.Venue;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@MappedEntity("sector")
public record Sector(
        @Id @GeneratedValue @Nullable Long id,
        @Relation(value = Relation.Kind.MANY_TO_ONE) @NotNull @MappedProperty("venue_id") Venue venue,
        @NotBlank @MappedProperty("name") String name,
        @NotNull @MappedProperty("total_capacity") Integer totalCapacity,
        @NotNull @MappedProperty("price") BigDecimal price,
        @NotNull @MappedProperty("type") SectorType type,
        @NotBlank @Nullable @MappedProperty("description") String description,
        @NotNull @Nullable @MappedProperty("seats_per_row") Integer seatsPerRow) {}
