package com.ticketleader.api.dto.event;

import com.ticketleader.domain.event.Event;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record EventSearchResponseDTO(Long id, String title) {

    public static EventSearchResponseDTO from(Event event) {
        return new EventSearchResponseDTO(event.id(), event.title());
    }
}
