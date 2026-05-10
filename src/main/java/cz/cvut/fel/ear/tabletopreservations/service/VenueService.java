package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGameItem;
import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.repository.VenueRepository;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.VenueRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing venues and their related entities.
 */
@Service
@Transactional
public class VenueService {

    private final VenueRepository venueRepository;

    @Autowired
    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    /**
     * Returns all venues.
     */
    @Transactional(readOnly = true)
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }

    /**
     * Finds a venue by ID.
     */
    @Transactional(readOnly = true)
    public Venue find(Integer id) {
        Objects.requireNonNull(id);

        Venue venue = venueRepository.find(id);

        if (venue == null) {
            throw NotFoundException.create("Venue", id);
        }
        return venue;
    }

    /**
     * Finds venues by city.
     */
    @Transactional(readOnly = true)
    public List<Venue> findByCity(String city) {
        return venueRepository.findByCity(city);
    }

    /**
     * Persists a new venue.
     */
    public void persist(Venue venue) {
        Objects.requireNonNull(venue);
        venueRepository.persist(venue);
    }

    /**
     * Updates an existing venue.
     */
    public void update(Venue venue) {
        Objects.requireNonNull(venue);
        venueRepository.update(venue);
    }

    /**
     * Removes a venue.
     */
    public void remove(Venue venue) {
        Objects.requireNonNull(venue);
        venueRepository.remove(venue);
    }

    /**
     * Updates venue details from request data.
     */
    public void updateVenue(Venue original, VenueRequest request) {
        Objects.requireNonNull(original);
        Objects.requireNonNull(request);

        if (request.getName() != null) {
            original.setName(request.getName());
        }

        if (request.getStreet() != null) {
            original.setStreet(request.getStreet());
        }

        if (request.getCity() != null) {
            original.setCity(request.getCity());
        }

        if (request.getPostalCode() != null) {
            original.setPostalCode(request.getPostalCode());
        }

        if (request.getCountry() != null) {
            original.setCountry(request.getCountry());
        }

        venueRepository.update(original);
    }

    /**
     * Returns game tables assigned to the venue.
     */
    @Transactional(readOnly = true)
    public List<GameTable> getGameTables(Venue venue) {
        Objects.requireNonNull(venue);
        return venue.getGameTables();
    }

    /**
     * Assigns a game table to a venue.
     */
    public void addGameTable(Venue venue, GameTable gameTable) {
        Objects.requireNonNull(venue);
        Objects.requireNonNull(gameTable);

        gameTable.setVenue(venue);
        venue.addGameTable(gameTable);

        venueRepository.update(venue);
    }

    /**
     * Removes a game table from a venue.
     */
    public void removeGameTable(Venue venue, GameTable gameTable) {
        Objects.requireNonNull(venue);
        Objects.requireNonNull(gameTable);

        venue.removeGameTable(gameTable);

        venueRepository.update(venue);
    }

    /**
     * Returns all time slots belonging to the venue.
     */
    @Transactional(readOnly = true)
    public List<TimeSlot> getTimeSlots(Venue venue) {
        Objects.requireNonNull(venue);
        return venue.getTimeSlots();
    }

    /**
     * Returns desk game items available at the venue.
     */
    @Transactional(readOnly = true)
    public List<DeskGameItem> getDeskGameItems(Venue venue) {
        Objects.requireNonNull(venue);
        return venue.getDeskGameItems();
    }

    /**
     * Adds a desk game item to a venue.
     */
    public void addDeskGameItem(Venue venue, DeskGameItem deskGameItem) {
        Objects.requireNonNull(venue);
        Objects.requireNonNull(deskGameItem);

        deskGameItem.setVenue(venue);
        venue.addDeskGameItem(deskGameItem);

        venueRepository.update(venue);
    }

    /**
     * Removes a desk game item from a venue.
     */
    public void removeDeskGameItem(Venue venue, DeskGameItem deskGameItem) {
        Objects.requireNonNull(venue);
        Objects.requireNonNull(deskGameItem);

        venue.removeDeskGameItem(deskGameItem);

        venueRepository.update(venue);
    }
}
