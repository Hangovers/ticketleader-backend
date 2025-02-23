package com.ticketleader.api.dto.reservee;

import com.ticketleader.domain.reservee.Gender;
import com.ticketleader.domain.reservee.Reservee;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public record ReserveeDTO(
        @Nullable Long id, // Optional: for updates.
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email @NotBlank String email,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String gender // Will be converted to Gender enum.
        ) {
    public Reservee toEntity() {
        return new Reservee(id(), firstName(), lastName(), email(), username(), password(), Gender.valueOf(gender()));
    }
}
