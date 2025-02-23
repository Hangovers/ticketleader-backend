package com.ticketleader.api.dto.venue;

import com.ticketleader.api.dto.sector.SectorResponseDTO;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;

@Serdeable
@Introspected
public record VenueResponseDTO(
        Long id, String name, String address, String description, List<SectorResponseDTO> sectors) {}
