package com.ticketleader.api.dto.reservee;

import com.ticketleader.domain.reservee.Reservee;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record ReserveeResponseDTO(
        Long id, String firstName, String lastName, String email, String username, String gender) {

    public static ReserveeResponseDTO fromEntity(Reservee reservee) {
        return new ReserveeResponseDTO(
                reservee.id(),
                reservee.firstName(),
                reservee.firstName(),
                reservee.email(),
                reservee.username(),
                reservee.gender().name());
    }
}
