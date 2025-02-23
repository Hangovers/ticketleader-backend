package com.ticketleader.api.dto.event;

import com.ticketleader.api.dto.venue.VenueRequestDTO;
import com.ticketleader.domain.event.Event;
import com.ticketleader.domain.venue.Venue;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Serdeable
@Introspected
public record EventDTO(
        @Nullable @NotBlank long id,
        @NotBlank String title,
        @NotNull LocalDateTime eventDate,
        @NotNull Integer capacity,
        @NotNull VenueRequestDTO venue) {
    /**
     * Converts this DTO into a domain Event.
     * The nested VenueRequestDTO is assumed to contain the full venue data.
     */
    public Event toEntity() {
        Venue venueEntity = venue.toEntity();
        return new Event(id, title, eventDate, capacity, venueEntity);
    }
}
