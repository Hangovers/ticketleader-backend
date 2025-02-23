package com.ticketleader.api.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.security.authentication.AuthenticationException;
import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Produces
@Singleton
@Requires(classes = {Exception.class, ExceptionHandler.class})
public class GlobalExceptionHandler implements ExceptionHandler<Exception, HttpResponse<Object>> {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Override
    public HttpResponse<Object> handle(HttpRequest request, Exception exception) {
        return switch (exception) {
            case IllegalStateException e -> {
                LOG.warn("Bad request: {}", e.getMessage());
                yield HttpResponse.badRequest();
            }
            case IllegalArgumentException e -> {
                LOG.warn("Not found: {}", e.getMessage());
                yield HttpResponse.notFound();
            }
            case ConstraintViolationException e -> {
                LOG.warn("Validation failed: {}", e.getMessage());
                yield HttpResponse.badRequest();
            }
            case AuthenticationException e -> {
                LOG.warn("Unauthorized: {}", e.getMessage());
                yield HttpResponse.unauthorized();
            }
            default -> {
                LOG.error("Unexpected error", exception);
                yield HttpResponse.serverError();
            }
        };
    }
}
