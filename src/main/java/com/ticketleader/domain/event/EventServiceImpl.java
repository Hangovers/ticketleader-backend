package com.ticketleader.domain.event;

import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class EventServiceImpl implements EventService {

    @Inject
    private EventRepository eventRepository;

    @Override
    public List<Event> getAllEvents(Pageable pageable) {
        return eventRepository.findAll(pageable).getContent();
    }

    @Override
    public Optional<Event> getEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    @Override
    public List<Event> searchEventsByTitle(String title, Pageable pageable) {
        return eventRepository.findByTitleContains(title, pageable);
    }

    @Override
    public List<Event> getEventsByVenue(Long venueId, Pageable pageable) {
        return eventRepository.findByVenueId(venueId, pageable);
    }
}
