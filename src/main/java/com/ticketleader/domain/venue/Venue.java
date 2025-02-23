package com.ticketleader.domain.venue;

import com.ticketleader.domain.event.Event;
import com.ticketleader.domain.sector.Sector;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@MappedEntity("venue")
public record Venue(
        @Id @GeneratedValue @Nullable Long id,
        @NotBlank @MappedProperty("name") String name,
        @NotBlank @MappedProperty("address") String address,
        @MappedProperty("description") String description,
        @Nullable @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "venue") List<Event> events,
        @Nullable @Relation(value = Relation.Kind.ONE_TO_MANY, mappedBy = "venue") List<Sector> sectors) {}
