package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.Reservation;
import cz.cvut.fel.ear.tabletopreservations.model.ReservationState;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Repository for managing Reservation entities.
 */
@Repository
public class ReservationRepository extends BaseRepository<Reservation> {

    public ReservationRepository() {
        super(Reservation.class);
    }

    /**
     * Finds all reservations created by the given user.
     */
    public List<Reservation> findReservationsByUser(User user) {
        List<Reservation> result = em.createNamedQuery("Reservation.findByUser", Reservation.class)
                .setParameter("user", user)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds the reservation that contains the given time slot.
     */
    public Reservation findByTimeSlot(TimeSlot timeSlot) {
        return em.createNamedQuery("Reservation.findByTimeSlot", Reservation.class)
                .setParameter("timeSlot", timeSlot)
                .getSingleResult();
    }

    /**
     * Finds all reservations that include any time slot of the given game table.
     */
    public List<Reservation> findByGameTable(GameTable gameTable) {
        List<Reservation> result = em.createNamedQuery("Reservation.findByGameTable", Reservation.class)
                .setParameter("gameTable", gameTable)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds reservations in a given state from a given date (inclusive).
     */
    public List<Reservation> findByStatusFromDate(ReservationState status, LocalDate fromDate) {
        List<Reservation> result = em.createNamedQuery("Reservation.findByStatusFromDate", Reservation.class)
                .setParameter("status", status)
                .setParameter("fromDate", fromDate)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
