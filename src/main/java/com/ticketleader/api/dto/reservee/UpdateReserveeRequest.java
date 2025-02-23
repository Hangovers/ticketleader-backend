package com.ticketleader.api.dto.reservee;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Serdeable
@Introspected
public record UpdateReserveeRequest(
        @NotBlank Long id, // Optional: for updates.
        @Nullable String firstName,
        @Nullable String lastName,
        @Email @NotBlank @Nullable String email,
        @Nullable String username,
        @Nullable String gender) {}
