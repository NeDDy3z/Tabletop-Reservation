package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGameItem;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.repository.DeskGameItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing desk game items.
 */
@Service
@Transactional
public class DeskGameItemService {

    private final DeskGameItemRepository deskGameItemRepository;

    @Autowired
    public DeskGameItemService(DeskGameItemRepository deskGameItemRepository) {
        this.deskGameItemRepository = deskGameItemRepository;
    }

    /**
     * Returns all desk game items.
     */
    @Transactional(readOnly = true)
    public List<DeskGameItem> findAll() {
        return deskGameItemRepository.findAll();
    }

    /**
     * Finds a desk game item by ID.
     */
    @Transactional(readOnly = true)
    public DeskGameItem find(Integer id) {
        Objects.requireNonNull(id);
        DeskGameItem deskGameItem = deskGameItemRepository.find(id);
        if (deskGameItem == null) {
            throw NotFoundException.create("DeskGameItem", id);
        }
        return deskGameItem;
    }

    /**
     * Finds all desk game items belonging to the given venue.
     */
    @Transactional(readOnly = true)
    public List<DeskGameItem> findByVenue(Venue venue) {
        Objects.requireNonNull(venue);
        return deskGameItemRepository.findByVenue(venue);
    }

    /**
     * Persists a new desk game item.
     */
    public void persist(DeskGameItem deskGameItem) {
        Objects.requireNonNull(deskGameItem);
        deskGameItemRepository.persist(deskGameItem);
    }

    /**
     * Updates an existing desk game item.
     */
    public void update(DeskGameItem deskGameItem) {
        Objects.requireNonNull(deskGameItem);
        deskGameItemRepository.update(deskGameItem);
    }

    /**
     * Removes a desk game item.
     */
    public void remove(DeskGameItem deskGameItem) {
        Objects.requireNonNull(deskGameItem);
        deskGameItemRepository.remove(deskGameItem);
    }
}
