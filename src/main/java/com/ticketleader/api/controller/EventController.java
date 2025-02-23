package com.ticketleader.api.controller;

import com.ticketleader.api.dto.event.AllEventsResponseDTO;
import com.ticketleader.api.dto.event.EventDetailsResponseDTO;
import com.ticketleader.api.dto.event.EventSearchResponseDTO;
import com.ticketleader.api.dto.sector.SectorResponseDTO;
import com.ticketleader.api.dto.venue.VenueResponseDTO;
import com.ticketleader.domain.event.Event;
import com.ticketleader.domain.event.EventService;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(value = "/events", produces = MediaType.APPLICATION_JSON)
public class EventController {

    @Inject
    private EventService eventService;

    @Get(produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "List Events", description = "Retrieves a paginated list of events with full details.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "List of events retrieved.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = AllEventsResponseDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<List<AllEventsResponseDTO>> getAllEvents(Pageable pageable) {
        return HttpResponse.ok(eventService.getAllEvents(pageable).stream()
                .map(AllEventsResponseDTO::fromEntity)
                .toList());
    }

    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Event by ID", description = "Retrieves event details for the specified event ID.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Event details retrieved successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = EventDetailsResponseDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Event not found.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<EventDetailsResponseDTO> getEventById(
            @Parameter(description = "The ID of the event to retrieve") Long id) {
        Optional<Event> eventOpt = eventService.getEventById(id);
        if (eventOpt.isPresent()) {
            Event e = eventOpt.get();
            EventDetailsResponseDTO dto = new EventDetailsResponseDTO(
                    e.id(),
                    e.title(),
                    e.eventDate(),
                    e.capacity(),
                    new VenueResponseDTO(
                            e.venue().id(),
                            e.venue().name(),
                            e.venue().address(),
                            e.venue().description(),
                            e.venue().sectors().stream()
                                    .map(SectorResponseDTO::fromEntity)
                                    .toList()));
            return HttpResponse.ok(dto);
        }
        return HttpResponse.notFound();
    }

    @Get(uri = "/search/{title}", produces = MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Search Events",
            description = "Search events by title. Returns events that match the provided text.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Matching events retrieved.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = EventSearchResponseDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<List<EventSearchResponseDTO>> searchEvents(
            @Parameter(description = "Title (or part of it) to search for") String title,
            @QueryValue Pageable pageable) {
        return HttpResponse.ok(eventService.searchEventsByTitle(title, pageable).stream()
                .map(EventSearchResponseDTO::from)
                .toList());
    }
}
