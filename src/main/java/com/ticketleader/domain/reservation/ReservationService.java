package com.ticketleader.domain.reservation;

import com.ticketleader.api.dto.reservation.ReservationDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing reservations.
 * Redis handles temporary seat locking; reservations are saved only upon successful payment.
 */
public interface ReservationService {
    List<Reservation> createReservations(Long mainReserveeId, List<ReservationDTO> reservations);

    String cancelReservation(Long reservationId, Long reserveeId);

    Optional<Reservation> getReservationById(Long reservationId);

    List<Reservation> getReservationByUserId(Long id);

    String updateReservationDetails(ReservationDTO reservation, String newReserveeEmail);
}
