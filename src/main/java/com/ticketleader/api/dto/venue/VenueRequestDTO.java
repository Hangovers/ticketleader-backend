package com.ticketleader.api.dto.venue;

import com.ticketleader.domain.venue.Venue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public record VenueRequestDTO(@Nullable Long id, @NotBlank String name, @NotBlank String address, String description) {
    public Venue toEntity() {
        return new Venue(id, name, address, description, null, null);
    }
}
