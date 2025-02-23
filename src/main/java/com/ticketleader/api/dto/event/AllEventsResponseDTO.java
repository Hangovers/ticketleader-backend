package com.ticketleader.api.dto.event;

import com.ticketleader.domain.event.Event;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.time.LocalDate;

@Serdeable
@Introspected
public record AllEventsResponseDTO(Long id, String title, LocalDate date, String location) {
    public static AllEventsResponseDTO fromEntity(final Event event) {
        return new AllEventsResponseDTO(
                event.id(),
                event.title(),
                event.eventDate().toLocalDate(),
                event.venue().name());
    }
}
