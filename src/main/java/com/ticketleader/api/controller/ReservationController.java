package com.ticketleader.api.controller;

import com.ticketleader.api.dto.reservation.ReservationDTO;
import com.ticketleader.api.dto.reservation.ReservationDetailsDTO;
import com.ticketleader.api.dto.reservation.ReservationResponseDTO;
import com.ticketleader.domain.reservation.Reservation;
import com.ticketleader.domain.reservation.ReservationService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.inject.Inject;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "/reservation", produces = "application/json")
public class ReservationController {

    @Inject
    private ReservationService reservationService;

    @Post(consumes = "application/json")
    @Operation(
            summary = "Create Reservations",
            description =
                    "Creates multiple reservations for a group booking after successful payment. "
                            + "The request DTO includes full event, sector, and reservee objects so that no extra lookups are required.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Reservations created successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = ReservationResponseDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Failed to create reservations.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<List<ReservationResponseDTO>> createReservations(
            Principal principal, List<ReservationDTO> request) {
        try {
            var authentication = (Authentication) principal;
            var reservations = reservationService.createReservations(
                    (Long) authentication.getAttributes().get("id"), request);

            return HttpResponse.ok(
                    reservations.stream().map(ReservationResponseDTO::from).collect(Collectors.toList()));
        } catch (Exception e) {
            return HttpResponse.badRequest();
        }
    }

    @Get(uri = "/{id}", produces = "application/json")
    @Operation(summary = "Get Reservation", description = "Retrieves reservation details by reservation ID.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservation details retrieved successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = ReservationResponseDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<ReservationResponseDTO> getReservationById(
            @Parameter(description = "The ID of the reservation") Long id, Principal principal) {
        Optional<Reservation> reservation = reservationService.getReservationById(id);
        var userDetails = (Authentication) principal;

        // Checks if a reservation is found and if it associated with the logged-in user
        if (reservation.isPresent()
                && ((Long) userDetails.getAttributes().get("id"))
                        .equals(reservation.get().reservee().id())) {
            return HttpResponse.ok(ReservationResponseDTO.from(reservation.get()));
        }
        return HttpResponse.notFound();
    }

    @Get(uri = "/user", produces = "application/json")
    @Operation(
            summary = "Get all the reservations of the authenticated user",
            description = "Retrieves reservation details by reservation ID for current user.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservations details retrieved successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = ReservationDetailsDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<List<ReservationDetailsDTO>> updateReservationById(Principal principal) {
        var currentUser = (Authentication) principal;

        return HttpResponse.ok(
                reservationService
                        .getReservationByUserId(
                                (Long) currentUser.getAttributes().get("id"))
                        .stream()
                        .map(ReservationDetailsDTO::from)
                        .toList());
    }

    @Put(uri = "transfer", produces = "application/json")
    @Operation(
            summary = "Let a user transfer one of his reservation to another registered user.",
            description = "Retrieves reservation details and updates the reservee information.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservations details updated successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
        @ApiResponse(
                responseCode = "401",
                description = "Unauthorized.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<String> updateReservationById(
            Principal principal, @Body ReservationDTO reservation, @Body String newReserveeEmail) {
        var currentUser = (Authentication) principal;

        if (((Long) currentUser.getAttributes().get("id"))
                .equals(reservation.reservee().id()))
            return HttpResponse.ok(reservationService.updateReservationDetails(reservation, newReserveeEmail));
        return HttpResponse.unauthorized();
    }

    @Delete(uri = "/{id}", produces = "application/json")
    @Operation(
            summary = "Get all the reservations of the authenticated user",
            description = "Retrieves reservation details by reservation ID for current user.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Reservations details retrieved successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = ReservationDetailsDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = "Reservation not found.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<String> updateReservationById(Principal principal, @Parameter Long id) {
        var currentUser = (Authentication) principal;
        return HttpResponse.ok(reservationService.cancelReservation(
                id, (Long) currentUser.getAttributes().get("id")));
    }
}
