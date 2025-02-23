package com.ticketleader.infrastructure.authentication;

import com.ticketleader.domain.reservee.Reservee;
import com.ticketleader.domain.reservee.ReserveeService;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class AuthenticationProviderUserPassword<B> implements HttpRequestAuthenticationProvider<B> {

    @Inject
    ReserveeService reserveeService;

    @Inject
    PasswordEncoder passwordEncoder;

    @Override
    public @NonNull AuthenticationResponse authenticate(
            @Nullable HttpRequest<B> requestContext, @NonNull AuthenticationRequest<String, String> authRequest) {

        Reservee reservee = reserveeService.findByUsername(authRequest.getIdentity());

        if (passwordEncoder.matches(authRequest.getSecret(), reservee.password())) {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", reservee.id());
            attributes.put("email", reservee.email());

            return AuthenticationResponse.success(authRequest.getIdentity(), attributes);
        }

        return AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH);
    }
}
