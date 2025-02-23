package com.ticketleader.infrastructure.messaging;

import com.ticketleader.api.dto.seat.SeatLockRequestDTO;
import com.ticketleader.domain.reservation.Reservation;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;
import java.util.List;

@KafkaClient
public interface SeatLockingProducer {
    @Topic("lock-seat")
    List<Reservation> sendContiguousSeatLockMessage(SeatLockRequestDTO seatLockRequestDTO);
}
