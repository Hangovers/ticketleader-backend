package com.ticketleader.api.controller;

import com.ticketleader.api.dto.seat.LockContiguousSeatRequestDTO;
import com.ticketleader.api.dto.seat.SeatDTO;
import com.ticketleader.domain.seat.SeatService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
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
import java.util.List;

@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "Bearer", bearerFormat = "JWT")
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "/seat", produces = "application/json")
public class SeatController {

    @Inject
    private SeatService seatService;

    @Get(uri = "/event/{eventId}/sector/{sectorId}", produces = "application/json")
    @Operation(
            summary = "Get Available Seats by Row",
            description = "Retrieves available seats in a given sector and row.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "List of available seats retrieved successfully.",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Integer.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<Integer> getAvailableSeatsByRow(
            @Parameter(description = "The ID of the sector") Long sectorId,
            @Parameter(description = "The ID of the event") Long eventId) {
        return HttpResponse.ok(seatService.getAvailableSeatCount(eventId, sectorId));
    }

    @Post(uri = "/contiguous", produces = "application/json")
    @Operation(
            summary = "Find Contiguous Seats and locks them for 5 minutes for the current user.",
            description = "Retrieves a block of contiguous available seats in a sector.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Contiguous seats found and locked successfully.",
                content =
                        @Content(
                                mediaType = "application/json",
                                schema = @Schema(type = "array", implementation = SeatDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = "Internal server error.",
                content =
                        @Content(mediaType = "application/json", schema = @Schema(implementation = HttpResponse.class)))
    })
    public HttpResponse<List<SeatDTO>> lockContiguousSeats(@Body LockContiguousSeatRequestDTO request) {
        return HttpResponse.ok(seatService.lockContiguousSeats(request).stream()
                .map(SeatDTO::fromEntity)
                .toList());
    }
}
