package com.ticketleader.domain.reservee;

import com.ticketleader.api.dto.reservee.ReserveeDTO;
import com.ticketleader.api.dto.reservee.UpdateReserveeRequest;
import java.util.Optional;

/**
 * Service interface for managing reservees.
 */
public interface ReserveeService {
    Reservee createReservee(ReserveeDTO reservee);

    Reservee getReserveeById(Long reserveeId);

    Reservee updateReservee(UpdateReserveeRequest reservee);

    Reservee findByUsername(String username);

    Reservee updatePassword(Long reserveeId, String newPassword);

    Optional<Reservee> findByEmail(String email);
}
