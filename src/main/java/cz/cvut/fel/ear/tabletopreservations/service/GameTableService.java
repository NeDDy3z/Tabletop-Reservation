package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.repository.GameTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing game tables and their time slots.
 */
@Service
@Transactional
public class GameTableService {

    private final GameTableRepository gameTableRepository;

    @Autowired
    public GameTableService(GameTableRepository gameTableRepository) {
        this.gameTableRepository = gameTableRepository;
    }

    /**
     * Returns all game tables.
     */
    @Transactional(readOnly = true)
    public List<GameTable> findAll() {
        return gameTableRepository.findAll();
    }

    /**
     * Finds a game table by ID.
     */
    @Transactional(readOnly = true)
    public GameTable find(Integer id) {
        Objects.requireNonNull(id);
        GameTable gameTable = gameTableRepository.find(id);
        if (gameTable == null) {
            throw NotFoundException.create("GameTable", id);
        }
        return gameTable;
    }

    /**
     * Persists a new game table.
     */
    public void persist(GameTable gameTable) {
        Objects.requireNonNull(gameTable);
        gameTableRepository.persist(gameTable);
    }

    /**
     * Updates an existing game table.
     */
    public void update(GameTable gameTable) {
        Objects.requireNonNull(gameTable);
        gameTableRepository.update(gameTable);
    }

    /**
     * Removes a game table.
     */
    public void remove(GameTable gameTable) {
        Objects.requireNonNull(gameTable);
        gameTableRepository.remove(gameTable);
    }

    /**
     * Returns all time slots for a given game table.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlots(GameTable gameTable) {
        Objects.requireNonNull(gameTable);
        return gameTable.getTimeSlots();
    }

    /**
     * Adds a new time slot to a game table.
     */
    public void addTimeSlot(GameTable gameTable, TimeSlot timeSlot) {
        Objects.requireNonNull(gameTable);
        Objects.requireNonNull(timeSlot);

        timeSlot.setGameTable(gameTable);
        gameTable.addTimeSlot(timeSlot);

        gameTableRepository.update(gameTable);
    }

    /**
     * Adds the given time slot definition to multiple game tables (creates a new TimeSlot instance per table).
     */
    public void addTimeSlotsToMultipleGameTables(List<GameTable> gameTables, TimeSlot timeSlot) {
        Objects.requireNonNull(gameTables);
        Objects.requireNonNull(timeSlot);

        for (GameTable gameTable : gameTables) {
            TimeSlot newTimeSlot = new TimeSlot();

            newTimeSlot.setStartTime(timeSlot.getStartTime());
            newTimeSlot.setEndTime(timeSlot.getEndTime());
            newTimeSlot.setGameTable(gameTable);
            gameTable.addTimeSlot(newTimeSlot);

            gameTableRepository.update(gameTable);
        }
    }

    /**
     * Removes a time slot from a game table.
     */
    public void removeTimeSlot(GameTable gameTable, TimeSlot timeSlot) {
        Objects.requireNonNull(gameTable);
        Objects.requireNonNull(timeSlot);

        gameTable.removeTimeSlot(timeSlot);
        gameTableRepository.update(gameTable);
    }
}
