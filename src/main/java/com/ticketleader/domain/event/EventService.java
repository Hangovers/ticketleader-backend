package com.ticketleader.domain.event;

import io.micronaut.data.model.Pageable;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing events.
 */
public interface EventService {
    List<Event> getAllEvents(Pageable pageable);

    Optional<Event> getEventById(Long eventId);

    List<Event> searchEventsByTitle(String title, Pageable pageable);

    List<Event> getEventsByVenue(Long venueId, Pageable pageable);
}
