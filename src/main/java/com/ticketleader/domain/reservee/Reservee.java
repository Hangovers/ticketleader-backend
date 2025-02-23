package com.ticketleader.domain.reservee;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@MappedEntity("reservee")
public record Reservee(
        @Id @GeneratedValue @Nullable Long id,
        @NotBlank @MappedProperty("first_name") String firstName,
        @NotBlank @MappedProperty("last_name") String lastName,
        @Email @NotBlank @MappedProperty("email") String email,
        @Nullable @MappedProperty("username") String username,
        @Nullable @MappedProperty("password") String password,
        @Nullable @MappedProperty("gender") Gender gender) {}
