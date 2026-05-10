package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.*;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Repository for managing TimeSlot entities.
 */
@Repository
public class TimeSlotRepository extends BaseRepository<TimeSlot> {

    public TimeSlotRepository() {
        super(TimeSlot.class);
    }

    /**
     * Finds all time slots belonging to the given reservation.
     */
    public List<TimeSlot> findByReservation(Reservation reservation) {
        List<TimeSlot> result = em.createNamedQuery("TimeSlot.findByReservation", TimeSlot.class)
                .setParameter("reservation", reservation)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all free time slots for the given game table on the given date.
     */
    public List<TimeSlot> findFreeByGameTableAndDate(GameTable gameTable, LocalDate date) {
        List<TimeSlot> result = em.createNamedQuery("TimeSlot.findFreeByGameTableAndDate", TimeSlot.class)
                .setParameter("table", gameTable)
                .setParameter("date", date)
                .setParameter("activeStates", List.of(
                        ReservationState.WAITING_FOR_CONFIRMATION,
                        ReservationState.CONFIRMED
                ))
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all free time slots for all game tables at the given venue on the given date.
     */
    public List<TimeSlot> findFreeByVenueAndDate(Venue venue, LocalDate date) {
        List<TimeSlot> result = em.createNamedQuery("TimeSlot.findFreeByVenueAndDate", TimeSlot.class)
                .setParameter("venue", venue)
                .setParameter("date", date)
                .setParameter("activeStates", List.of(
                        ReservationState.WAITING_FOR_CONFIRMATION,
                        ReservationState.CONFIRMED
                ))
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
