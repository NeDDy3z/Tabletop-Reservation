package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.TimeSlotResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.GameTableRequest;
import cz.cvut.fel.ear.tabletopreservations.security.SecurityService;
import cz.cvut.fel.ear.tabletopreservations.service.GameTableService;
import cz.cvut.fel.ear.tabletopreservations.service.TimeSlotService;
import cz.cvut.fel.ear.tabletopreservations.service.VenueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing game tables inside venues.
 *
 * Handles creation, update and removal of tables and their time slots.
 */
@RestController
@RequestMapping("/rest/gametables")
public class GameTableController {

    private static final Logger LOG = LoggerFactory.getLogger(GameTableController.class);

    private final GameTableService gameTableService;
    private final TimeSlotService timeSlotService;
    private final VenueService venueService;
    private final SecurityService securityService;

    @Autowired
    public GameTableController(
            GameTableService gameTableService,
            TimeSlotService timeSlotService,
            VenueService venueService,
            SecurityService securityService) {
        this.gameTableService = gameTableService;
        this.timeSlotService = timeSlotService;
        this.venueService = venueService;
        this.securityService = securityService;
    }

    /**
     * Returns all game tables.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GameTable> getGameTables() {
        return gameTableService.findAll();
    }

    /**
     * Returns a single game table by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public GameTable getGameTable(@PathVariable Integer id) {
        return gameTableService.find(id);
    }

    /**
     * Creates a new game table within a venue.
     * Accessible only to providers owning the venue or administrators.
     */
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createGameTable(@RequestBody GameTableRequest request) {
        final Venue venue = venueService.find(request.getVenueId());

        if (!securityService.isOwnerOrAdmin(venue)) {
            throw new AccessDeniedException("You do not have permission to add a game table to this venue.");
        }

        final GameTable gameTable = new GameTable();
        gameTable.setName(request.getName());
        gameTable.setCapacity(request.getCapacity());
        gameTable.setVenue(venue);

        gameTableService.persist(gameTable);

        LOG.debug("Created game table {}.", gameTable);

        return ResponseEntity.status(HttpStatus.OK).body(new CreatedResponse(gameTable.getId()));
    }

    /**
     * Updates an existing game table.
     * Accessible only to the table owner or administrators.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@gameTableService.find(#id))")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateGameTable(@PathVariable Integer id, @RequestBody GameTableRequest request) {
        final GameTable original = gameTableService.find(id);

        original.setName(request.getName());
        original.setCapacity(request.getCapacity());

        gameTableService.update(original);

        LOG.debug("Updated game table {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Deletes a game table.
     * Accessible only to the table owner or administrators.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@gameTableService.find(#id))")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGameTable(@PathVariable Integer id) {
        final GameTable gameTable = gameTableService.find(id);

        gameTableService.remove(gameTable);

        LOG.debug("Removed game table {}.", gameTable);
    }

    /**
     * Returns all time slots of the given game table.
     */
    @GetMapping(value = "/{id}/timeslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimeSlotResponse> getTimeSlots(@PathVariable Integer id) {
        final GameTable gameTable = gameTableService.find(id);

        return gameTable.getTimeSlots().stream()
                .map(TimeSlotResponse::fromTimeSlot)
                .toList();
    }

    /**
     * Adds a new time slot to a game table.
     * Accessible only to the table owner or administrators.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@gameTableService.find(#gameTableId))")
    @PostMapping(value = "/{gameTableId}/timeslots", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> addTimeSlot(@PathVariable Integer gameTableId, @RequestBody TimeSlot timeSlot) {
        final GameTable gameTable = gameTableService.find(gameTableId);

        gameTableService.addTimeSlot(gameTable, timeSlot);

        LOG.debug("Added time slot {} to game table {}.", timeSlot, gameTable);

        return ResponseEntity.status(HttpStatus.OK).body(new CreatedResponse(timeSlot.getId()));
    }

    /**
     * Updates an existing time slot of a game table.
     * Accessible only to the table owner or administrators.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@gameTableService.find(#gameTableId))")
    @PutMapping(value = "/{gameTableId}/timeslots/{timeSlotId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateTimeSlot(@PathVariable Integer gameTableId, @PathVariable Integer timeSlotId, @RequestBody TimeSlot timeSlot) {
        final GameTable gameTable = gameTableService.find(gameTableId);
        final TimeSlot original = timeSlotService.find(timeSlotId);

        original.setStartTime(timeSlot.getStartTime());
        original.setEndTime(timeSlot.getEndTime());

        timeSlotService.update(original);

        LOG.debug("Updated time slot {} of game table {}.", original, gameTable);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Removes a time slot from a game table.
     * Accessible only to the table owner or administrators.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@gameTableService.find(#gameTableId))")
    @DeleteMapping(value = "/{gameTableId}/timeslots/{timeSlotId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTimeSlot(@PathVariable Integer gameTableId, @PathVariable Integer timeSlotId) {
        final GameTable gameTable = gameTableService.find(gameTableId);
        final TimeSlot timeSlot = timeSlotService.find(timeSlotId);

        gameTableService.removeTimeSlot(gameTable, timeSlot);

        LOG.debug("Removed time slot {} from game table {}.", timeSlot, gameTable);
    }
}
