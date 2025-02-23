package com.ticketleader.api.dto.sector;

import com.ticketleader.domain.sector.Sector;
import com.ticketleader.domain.sector.SectorType;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import java.math.BigDecimal;

@Serdeable
@Introspected
public record SectorResponseDTO(
        Long id,
        String name,
        Integer totalCapacity,
        BigDecimal price,
        SectorType type,
        String description,
        Integer seatsPerRow) {
    public static SectorResponseDTO fromEntity(Sector sector) {
        return new SectorResponseDTO(
                sector.id(),
                sector.name(),
                sector.totalCapacity(),
                sector.price(),
                sector.type(),
                sector.description(),
                sector.seatsPerRow());
    }
}
