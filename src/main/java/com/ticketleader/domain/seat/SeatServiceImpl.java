package com.ticketleader.domain.seat;

import com.ticketleader.api.dto.seat.LockContiguousSeatRequestDTO;
import com.ticketleader.api.dto.seat.SeatLockRequestDTO;
import com.ticketleader.infrastructure.messaging.SeatLockingProducer;
import io.micronaut.http.server.exceptions.InternalServerException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Singleton
public class SeatServiceImpl implements SeatService {

    @Inject
    private SeatRepository seatRepository;

    @Inject
    private SeatLockingService seatLockingService;

    @Inject
    private SeatLockingProducer seatLockingProducer;

    @Override
    public Integer getAvailableSeatCount(Long eventId, Long sectorId) {
        List<Seat> allSeats = getSeatsBySector(sectorId);

        // Get locked seat IDs specific to this event.
        Set<Long> lockedSeatIds = seatLockingService.getLockedSeatIds(eventId);
        long count = allSeats.stream()
                .filter(seat -> !lockedSeatIds.contains(seat.id()))
                .count();
        return (int) count;
    }

    @Override
    public List<Seat> lockContiguousSeats(LockContiguousSeatRequestDTO request) {
        // Get all available seats (ordered by seat number) in this sector
        List<Seat> seats = getSeatsBySector(request.sectorId());

        // Retrieve all locked seat IDs
        Set<Long> lockedSeatIds = seatLockingService.getLockedSeatIds(request.eventId());

        // Remove seats that are locked
        seats.removeIf(seat -> lockedSeatIds.contains(seat.id()));

        // Group seats by row using the formula: row = (seatNumber - 1) / seatsPerRow
        Map<Integer, List<Seat>> seatsByRow =
                seats.stream().collect(Collectors.groupingBy(seat -> (seat.seatNumber() - 1) / request.seatsPerRow()));

        // Iterate over each row
        for (Map.Entry<Integer, List<Seat>> entry : seatsByRow.entrySet()) {
            List<Seat> rowSeats = entry.getValue();

            // Skip rows that can't have enough contiguous seats
            if (!canRowHaveContiguousSeats(rowSeats, request.seatsPerRow(), request.requestedSeats())) {
                continue;
            }

            for (int i = 0; i < rowSeats.size(); i++) {
                List<Seat> expectedContiguousSeat = rowSeats.subList(i, i + request.requestedSeats());

                if (areSeatsContiguous(
                        expectedContiguousSeat, rowSeats.get(i).seatNumber(), request.requestedSeats())) {

                    List<Long> seatIds =
                            expectedContiguousSeat.stream().map(Seat::id).toList();

                    seatLockingProducer.sendContiguousSeatLockMessage(createLockRequest(request, seatIds));

                    return expectedContiguousSeat;
                }
            }
        }
        throw new InternalServerException(
                """
                There are not enough available seats in this sector.
                Please retry later or choose another sector.
                """);
    }

    private List<Seat> getSeatsBySector(Long sectorId) {
        return seatRepository.findBySectorIdOrderBySeatNumberAsc(sectorId);
    }

    private boolean canRowHaveContiguousSeats(List<Seat> rowSeats, int seatsPerRow, int requestedSeats) {
        if (rowSeats.size() < requestedSeats) return false;

        // Check if the first and last possible seats would be in the same row
        int firstSeatRowPosition = rowSeats.getFirst().seatNumber() % seatsPerRow;
        int lastPossibleSeatRowPosition = (rowSeats.getFirst().seatNumber() + requestedSeats - 1) % seatsPerRow;

        return firstSeatRowPosition <= (seatsPerRow - requestedSeats + 1);
    }

    private boolean areSeatsContiguous(List<Seat> seats, int startSeatNumber, int size) {
        if (seats.size() < size) return false;
        for (int i = 0; i < size; i++) {
            if (seats.get(i).seatNumber() != startSeatNumber + i) {
                return false;
            }
        }
        return true;
    }

    private static SeatLockRequestDTO createLockRequest(LockContiguousSeatRequestDTO request, List<Long> seatIds) {
        return new SeatLockRequestDTO(request.eventId(), request.sectorId(), request.reserveeId(), seatIds);
    }
}
