package com.ticketleader.api.controller;

import com.ticketleader.api.dto.AuthRequest;
import com.ticketleader.infrastructure.authentication.AuthenticationProviderUserPassword;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.generator.JwtTokenGenerator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Controller("/auth")
@Secured(SecurityRule.IS_ANONYMOUS)
public class AuthenticationController {

    @Inject
    private AuthenticationProviderUserPassword authenticationProvider;

    @Inject
    private JwtTokenGenerator jwtTokenGenerator;

    @Post(uri = "/login", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    @Operation(summary = "login endpoint", description = "Lets a registered reservee login to his account.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Returns an authorization token.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema =
                                        @Schema(
                                                type = "array",
                                                implementation = Map.class,
                                                example =
                                                        """
                                                        {
                                                            "token": "eetriyergouo8iory254725hreiu.truwi9tiowutohho";
                                                        }
                                                        """))),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized response.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<?> login(AuthRequest authRequest) {
        var authResponse = authenticationProvider.authenticate(null, authRequest);
        Optional<String> token = authResponse.getAuthentication().isPresent()
                ? jwtTokenGenerator.generateToken(
                        authResponse.getAuthentication().get(), 3600)
                : Optional.empty();

        if (token.isEmpty()) return HttpResponse.unauthorized();

        return HttpResponse.ok(Collections.singletonMap("token", token.get()));
    }
}
