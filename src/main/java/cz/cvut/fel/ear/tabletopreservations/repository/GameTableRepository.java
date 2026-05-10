package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.Reservation;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Repository for managing game table entities.
 */
@Repository
public class GameTableRepository extends BaseRepository<GameTable> {

    public GameTableRepository() {
        super(GameTable.class);
    }

    /**
     * Finds the game table that owns the given time slot.
     */
    public GameTable findByTimeSlot(TimeSlot timeSlot) {
        return em.createNamedQuery("GameTable.findByTimeSlot", GameTable.class)
                .setParameter("timeSlot", timeSlot)
                .getSingleResult();
    }

    /**
     * Finds all game tables participating in the given reservation.
     */
    public List<GameTable> findAllByReservation(Reservation reservation) {
        List<GameTable> result = em.createNamedQuery("GameTable.findAllByReservation", GameTable.class)
                .setParameter("reservation", reservation)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
