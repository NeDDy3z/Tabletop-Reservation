package cz.cvut.fel.ear.tabletopreservations.service;
import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.repository.*;
import cz.cvut.fel.ear.tabletopreservations.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Service responsible for reservation lifecycle (create/confirm/cancel/complete) and reservation queries.
 */
@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, TimeSlotRepository timeSlotRepository) {
        this.reservationRepository = reservationRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Returns all reservations.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    /**
     * Finds a reservation by its id.
     */
    @Transactional(readOnly = true)
    public Reservation find(Integer id) {
        Objects.requireNonNull(id);
        Reservation reservation = reservationRepository.find(id);
        if (reservation == null) {
            throw NotFoundException.create("Reservation", id);
        }
        return reservation;
    }

    /**
     * Returns reservations created by the given user.
     */
    @Transactional(readOnly = true)
    public List<Reservation> findByUser(User user) {
        Objects.requireNonNull(user);
        return reservationRepository.findReservationsByUser(user);
    }

    /**
     * Persists a new reservation.
     */
    public void persist(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservationRepository.persist(reservation);
    }

    /**
     * Updates an existing reservation.
     */
    public void update(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservationRepository.update(reservation);
    }

    /**
     * Removes a reservation.
     */
    public void remove(Reservation reservation) {
        Objects.requireNonNull(reservation);
        reservationRepository.remove(reservation);
    }

    /**
     * Creates a new reservation for given user, date and time slots.
     */
    @Transactional
    public Reservation createReservation(User user, LocalDate date, List<TimeSlot> slots) {
        Objects.requireNonNull(user, "User must not be null");
        Objects.requireNonNull(date, "Date must not be null");

        for (TimeSlot slot : slots) {
            Objects.requireNonNull(slot, "Time slot must not be null");
            List<TimeSlot> freeForTable = timeSlotRepository.findFreeByGameTableAndDate(slot.getGameTable(), date);
            if (freeForTable.isEmpty()) {
                throw new IllegalArgumentException("No time slots are available for the given date");
            }
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setDate(date);
        reservation.setStatus(ReservationState.WAITING_FOR_CONFIRMATION);

        for (TimeSlot slot : slots) {
            reservation.addTimeSlot(slot);
        }

        reservationRepository.persist(reservation);
        return reservation;
    }

    /**
     * Confirms a reservation.
     */
    @Transactional
    public void confirmReservation(Reservation reservation) {
        Objects.requireNonNull(reservation, "Reservation must not be null");
        reservation.setStatus(ReservationState.CONFIRMED);
        reservationRepository.update(reservation);
    }

    /**
     * Cancels an existing reservation on behalf of the given user.
     */
    @Transactional
    public void cancelReservation(Reservation reservation, User user) {
        Objects.requireNonNull(user, "User must not be null");
        Objects.requireNonNull(reservation, "Reservation must not be null");

        if (user.getRoles().contains(Role.PLAYER)) {
            reservation.setStatus(ReservationState.CANCELED_BY_PLAYER);
        }
        if (user.getRoles().contains(Role.PROVIDER)) {
            reservation.setStatus(ReservationState.REJECTED);
        }

        reservationRepository.update(reservation);
    }

    /**
     * Completes an existing reservation.
     */
    @Transactional
    public void completeReservation(Reservation reservation, boolean successful) {
        Objects.requireNonNull(reservation, "Reservation must not be null");

        if (successful) {
            reservation.setStatus(ReservationState.COMPLETED);
        } else {
            reservation.setStatus(ReservationState.FAILED_TO_ARRIVE);
        }

        reservationRepository.update(reservation);
    }
}
