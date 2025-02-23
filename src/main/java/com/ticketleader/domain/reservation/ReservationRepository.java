package com.ticketleader.domain.reservation;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Join("seat")
    @Join("event.venue")
    @Join("sector")
    @Join("reservee")
    Optional<Reservation> findById(Long id);

    @Join("seat")
    @Join("event.venue")
    @Join("sector")
    @Join("reservee")
    List<Reservation> findByReserveeId(Long reserveeId);
}
