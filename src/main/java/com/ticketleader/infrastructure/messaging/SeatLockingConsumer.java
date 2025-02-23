package com.ticketleader.infrastructure.messaging;

import com.ticketleader.api.dto.seat.SeatLockRequestDTO;
import com.ticketleader.domain.seat.SeatLockingService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@KafkaListener
@Singleton
public class SeatLockingConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(SeatLockingConsumer.class);

    @Inject
    private SeatLockingService seatLockService;

    /**
     * Consumes messages from the "reservation-locked" topic.
     * The message payload includes a ReservationRequestDTO and a reserveeId.
     */
    @Topic("lock-seat")
    public void receiveReservationLocked(SeatLockRequestDTO lockRequest) {
        LOG.info(
                "Received seat lock message for event {}, sector {}, reservee {}",
                lockRequest.eventId(),
                lockRequest.sectorId(),
                lockRequest.reserveeId());

        try {
            lockRequest
                    .seatIds()
                    .forEach(seatId -> seatLockService.lockSeat(
                            lockRequest.eventId(), lockRequest.sectorId(), seatId, lockRequest.reserveeId()));
            LOG.info(
                    "Processed seat lock message successfully for event {}, sector {}, reservee {}",
                    lockRequest.eventId(),
                    lockRequest.sectorId(),
                    lockRequest.reserveeId());
        } catch (Exception e) {
            LOG.error(
                    "Error processing seat lock message for event {} and reservee {}: {}",
                    lockRequest.eventId(),
                    lockRequest.reserveeId(),
                    e.getMessage(),
                    e);
        }
    }
}
