package com.ticketleader.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Introspected
public record AuthRequest(String username, String password) implements AuthenticationRequest<String, String> {
    @Override
    public String getIdentity() {
        return username;
    }

    @Override
    public String getSecret() {
        return password;
    }
}
