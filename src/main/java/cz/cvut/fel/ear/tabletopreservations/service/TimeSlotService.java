package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Service providing CRUD operations and availability queries for TimeSlot.
 */
@Service
@Transactional
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    public TimeSlotService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    /**
     * Returns all time slots.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findAll() {
        return timeSlotRepository.findAll();
    }

    /**
     * Finds a time slot by its id.
     */
    @Transactional(readOnly = true)
    public TimeSlot find(Integer id) {
        Objects.requireNonNull(id);
        TimeSlot timeSlot = timeSlotRepository.find(id);
        if (timeSlot == null) {
            throw NotFoundException.create("TimeSlot", id);
        }
        return timeSlot;
    }

    /**
     * Returns free time slots for the given game table and date.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findFreeByGameTableAndDate(GameTable gameTable, LocalDate date) {
        Objects.requireNonNull(gameTable);
        Objects.requireNonNull(date);
        return timeSlotRepository.findFreeByGameTableAndDate(gameTable, date);
    }

    /**
     * Returns free time slots for the given venue and date.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> findFreeByVenueAndDate(Venue venue, LocalDate date) {
        Objects.requireNonNull(venue);
        Objects.requireNonNull(date);
        return timeSlotRepository.findFreeByVenueAndDate(venue, date);
    }

    /**
     * Persists a new time slot.
     */
    public void persist(TimeSlot timeSlot) {
        Objects.requireNonNull(timeSlot);
        timeSlotRepository.persist(timeSlot);
    }

    /**
     * Updates an existing time slot.
     */
    public void update(TimeSlot timeSlot) {
        Objects.requireNonNull(timeSlot);
        timeSlotRepository.update(timeSlot);
    }

    /**
     * Removes a time slot.
     */
    public void remove(TimeSlot timeSlot) {
        Objects.requireNonNull(timeSlot);
        timeSlotRepository.remove(timeSlot);
    }
}
