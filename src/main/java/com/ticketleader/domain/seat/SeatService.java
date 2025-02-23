package com.ticketleader.domain.seat;

import com.ticketleader.api.dto.seat.LockContiguousSeatRequestDTO;
import java.util.List;

/**
 * Service interface for managing seats in numbered sectors.
 */
public interface SeatService {
    Integer getAvailableSeatCount(Long eventId, Long sectorId);

    List<Seat> lockContiguousSeats(LockContiguousSeatRequestDTO request);
}
