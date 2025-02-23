package com.ticketleader.api.dto.sector;

import com.ticketleader.api.dto.venue.VenueRequestDTO;
import com.ticketleader.domain.sector.Sector;
import com.ticketleader.domain.sector.SectorType;
import com.ticketleader.domain.venue.Venue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Serdeable
@Introspected
public record SectorDTO(
        @Nullable Long id,
        @NotNull VenueRequestDTO venue,
        @NotBlank String name,
        @NotNull Integer totalCapacity,
        @NotNull BigDecimal price,
        @NotNull SectorType type,
        @Nullable String description,
        @Nullable Integer seatsPerRow) {
    public Sector toEntity() {
        Venue venueEntity = venue.toEntity();
        return new Sector(id, venueEntity, name, totalCapacity, price, type, description, seatsPerRow);
    }
}
