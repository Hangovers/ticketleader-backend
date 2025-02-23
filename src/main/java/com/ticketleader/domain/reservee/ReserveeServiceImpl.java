package com.ticketleader.domain.reservee;

import com.ticketleader.api.dto.reservee.ReserveeDTO;
import com.ticketleader.api.dto.reservee.UpdateReserveeRequest;
import com.ticketleader.infrastructure.authentication.PasswordEncoder;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;

@Singleton
public class ReserveeServiceImpl implements ReserveeService {

    @Inject
    private ReserveeRepository reserveeRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Override
    public Reservee createReservee(ReserveeDTO dto) {
        return reserveeRepository.save(new Reservee(
                dto.id(),
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.username(),
                passwordEncoder.encode(dto.password()),
                Gender.valueOf(dto.gender())));
    }

    @Override
    public Reservee getReserveeById(Long reserveeId) {
        Optional<Reservee> optionalReservee = reserveeRepository.findById(reserveeId);
        if (optionalReservee.isEmpty()) throw new IllegalArgumentException("Reservee does not exist");
        return optionalReservee.get();
    }

    @Override
    public Reservee updateReservee(UpdateReserveeRequest reserveeRequest) {
        Optional<Reservee> existing = reserveeRepository.findById(reserveeRequest.id());
        return reserveeRepository.update(getUpdated(reserveeRequest, existing));
    }

    @Override
    public Reservee findByUsername(String username) {
        Optional<Reservee> optionalReservee = reserveeRepository.findByUsername(username);
        if (optionalReservee.isEmpty()) throw new IllegalArgumentException("Reservee does not exist");
        return optionalReservee.get();
    }

    @Override
    public Reservee updatePassword(Long reserveeId, String newPassword) {
        Optional<Reservee> existing = reserveeRepository.findById(reserveeId);
        if (existing.isEmpty()) throw new IllegalArgumentException("Reservee does not exist");

        Reservee reservee = existing.get();

        if (passwordEncoder.matches(newPassword, reservee.password()))
            throw new IllegalArgumentException("New password cannot be the same as the old password");

        return reserveeRepository.save(new Reservee(
                reservee.id(),
                reservee.firstName(),
                reservee.lastName(),
                reservee.email(),
                reservee.username(),
                passwordEncoder.encode(newPassword),
                reservee.gender()));
    }

    @Override
    public Optional<Reservee> findByEmail(String email) {
        return reserveeRepository.findByEmail(email);
    }

    private static Reservee getUpdated(UpdateReserveeRequest reserveeRequest, Optional<Reservee> existing) {
        if (existing.isEmpty()) throw new IllegalArgumentException("Reservee does not exist");

        Reservee reservee = existing.get();

        String firstName = reserveeRequest.firstName().isBlank() ? reservee.firstName() : reserveeRequest.firstName();
        String lastName = reserveeRequest.lastName().isBlank() ? reservee.lastName() : reserveeRequest.lastName();
        String email = reserveeRequest.email().isBlank() ? reservee.email() : reserveeRequest.email();
        String username = reserveeRequest.username().isBlank() ? reservee.username() : reserveeRequest.username();
        Gender gender =
                reserveeRequest.gender().isBlank() ? reservee.gender() : Gender.valueOf(reserveeRequest.gender());

        return new Reservee(reserveeRequest.id(), firstName, lastName, email, username, reservee.password(), gender);
    }
}
