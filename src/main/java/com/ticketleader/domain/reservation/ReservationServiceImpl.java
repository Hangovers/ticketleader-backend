package com.ticketleader.domain.reservation;

import com.ticketleader.api.dto.reservation.ReservationDTO;
import com.ticketleader.domain.reservee.Reservee;
import com.ticketleader.domain.reservee.ReserveeService;
import com.ticketleader.domain.seat.SeatLockingService;
import io.micronaut.http.server.exceptions.NotFoundException;
import io.micronaut.security.authentication.AuthenticationException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class ReservationServiceImpl implements ReservationService {

    @Inject
    private ReservationRepository reservationRepository;

    @Inject
    private SeatLockingService seatLockingService;

    @Inject
    private ReserveeService reserveeService;

    @Override
    public List<Reservation> createReservations(Long mainReserveeId, List<ReservationDTO> reservationRequests) {
        List<Reservation> reservations = new ArrayList<>();
        for (ReservationDTO reservationRequest : reservationRequests) {

            // Verify that the seat is locked by the mainReservee.
            if (!seatLockingService.isSeatLockedBy(
                    reservationRequest.event().id(),
                    reservationRequest.sector().id(),
                    reservationRequest.seat().id(),
                    mainReserveeId)) {
                throw new IllegalStateException(
                        "Seat " + reservationRequest.seat().id() + " is not locked by reservee " + mainReserveeId);
            }

            reservations.add(reservationRequest.toEntity());
        }

        return reservationRepository.saveAll(reservations);
    }

    @Override
    public Optional<Reservation> getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId);
    }

    @Override
    public List<Reservation> getReservationByUserId(Long id) {
        return reservationRepository.findByReserveeId(id);
    }

    @Override
    public String updateReservationDetails(ReservationDTO reservation, String newReserveeEmail) {
        Optional<Reservee> newReservee = reserveeService.findByEmail(newReserveeEmail);

        if (newReservee.isPresent()) {
            Reservation reservationToUpdate = reservation.toEntity();
            Reservation updatedReservation = new Reservation(
                    reservationToUpdate.id(),
                    reservationToUpdate.event(),
                    reservationToUpdate.sector(),
                    newReservee.get(),
                    reservationToUpdate.seat(),
                    LocalDateTime.now());

            reservationRepository.update(updatedReservation);
            return "Reservation updated successfully";
        }

        throw new NotFoundException();
    }

    @Override
    public String cancelReservation(Long reservationId, Long reserveeId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElse(null);

        if (reservation != null && reserveeId.equals(reservation.reservee().id())) {
            reservationRepository.delete(reservation);
            return "Reservation cancelled successfully";
        } else if (reservation == null) throw new NotFoundException();
        else throw new AuthenticationException();
    }
}
