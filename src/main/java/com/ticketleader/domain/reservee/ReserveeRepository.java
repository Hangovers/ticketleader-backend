package com.ticketleader.domain.reservee;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.Valid;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.POSTGRES)
public interface ReserveeRepository extends CrudRepository<Reservee, Long> {
    Optional<Reservee> findByEmail(@NonNull @Valid String email);

    Optional<Reservee> findByUsername(@NonNull String username);
}
