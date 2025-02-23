package com.ticketleader.api.controller;

import com.ticketleader.api.dto.reservee.ReserveeDTO;
import com.ticketleader.api.dto.reservee.ReserveeResponseDTO;
import com.ticketleader.api.dto.reservee.UpdateReserveeRequest;
import com.ticketleader.domain.reservee.ReserveeService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.inject.Inject;
import java.security.Principal;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "/reservee", produces = "application/json")
public class ReserveeController {

    @Inject
    private ReserveeService reserveeService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Post(consumes = "application/json")
    @Operation(
            summary = "Create Reservee",
            description = "Registers a new reservee with full details.",
            security = {})
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Reservee created successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ReserveeResponseDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Failed to create reservee.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<ReserveeResponseDTO> createReservee(@Body ReserveeDTO request) {
        try {
            var reservee = reserveeService.createReservee(request);
            var response = new ReserveeResponseDTO(
                    reservee.id(),
                    reservee.firstName(),
                    reservee.lastName(),
                    reservee.email(),
                    reservee.username(),
                    reservee.gender().name());
            return HttpResponse.ok(response);
        } catch (Exception e) {
            return HttpResponse.badRequest();
        }
    }

    @Get(produces = "application/json")
    @Operation(summary = "Get Reservee", description = "Retrieves logged user details by their id.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservee details retrieved successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ReserveeResponseDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservee not found.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<ReserveeResponseDTO> getReserveeById(Principal principal) {
        var user = (Authentication) principal;
        return HttpResponse.ok(ReserveeResponseDTO.fromEntity(
                reserveeService.getReserveeById((Long) user.getAttributes().get("id"))));
    }

    @Put(consumes = "application/json")
    @Operation(summary = "Update Reservee", description = "Updates an existing reservee's information.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservee updated successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(
                responseCode = "401",
                description = "You are not authorized to update this user.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<String> updateReservee(Principal principal, @Body UpdateReserveeRequest request) {
        var user = (Authentication) principal;

        if (((Long) user.getAttributes().get("id")).equals(request.id())) {
            reserveeService.updateReservee(request);
            return HttpResponse.ok("Your data has been updated successfully.");
        }

        return HttpResponse.unauthorized();
    }

    @Put(uri = "/password", consumes = "application/json")
    @Operation(summary = "Update Reservee's password", description = "Updates current user's password.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservee's password successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Failed to update reservee.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<String> updatePassword(Principal principal, @Body String updatedPassword) {
        var user = (Authentication) principal;

        reserveeService.updatePassword((Long) user.getAttributes().get("id"), updatedPassword);
        return HttpResponse.ok("Password updated successfully.");
    }
}
