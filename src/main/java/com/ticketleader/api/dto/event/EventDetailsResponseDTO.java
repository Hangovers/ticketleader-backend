package com.ticketleader.api.dto.event;

import com.ticketleader.api.dto.venue.VenueResponseDTO;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDateTime;

@Serdeable
@Introspected
public record EventDetailsResponseDTO(
        Long id, String title, LocalDateTime eventDate, Integer capacity, VenueResponseDTO venue) {}
