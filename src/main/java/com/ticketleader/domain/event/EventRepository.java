package com.ticketleader.domain.event;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface EventRepository extends PageableRepository<Event, Long> {
    @Join("venue")
    Optional<Event> findById(Long id);

    @Join("venue")
    List<Event> findByTitleContains(String title, Pageable pageable);

    @Join("venue")
    List<Event> findByVenueId(Long venueId, Pageable pageable);

    @Override
    @Join("venue")
    @NonNull
    Page<Event> findAll(Pageable pageable);
}
