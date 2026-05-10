package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.TimeSlotResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.TimeSlotBulkRequest;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.TimeSlotRequest;
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
 * REST controller for managing time slots.
 */
@RestController
@RequestMapping("/rest/timeslots")
public class TimeSlotController {

    private static final Logger LOG = LoggerFactory.getLogger(TimeSlotController.class);

    private final GameTableService gameTableService;
    private final SecurityService securityService;
    private final TimeSlotService timeSlotService;
    private final VenueService venueService;

    @Autowired
    public TimeSlotController(
            GameTableService gameTableService,
            TimeSlotService timeSlotService,
            SecurityService securityService,
            VenueService venueService) {
        this.gameTableService = gameTableService;
        this.securityService = securityService;
        this.timeSlotService = timeSlotService;
        this.venueService = venueService;
    }

    /**
     * Returns all time slots.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimeSlotResponse> getTimeSlots() {
        return timeSlotService.findAll().stream().map(TimeSlotResponse::fromTimeSlot).toList();
    }

    /**
     * Returns a specific time slot by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TimeSlotResponse getTimeSlot(@PathVariable Integer id) {
        return TimeSlotResponse.fromTimeSlot(timeSlotService.find(id));
    }

    /**
     * Creates a new time slot for a game table.
     */
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createTimeSlot(@RequestBody TimeSlotRequest request) {
        final GameTable gameTable = gameTableService.find(request.getGameTableId());

        if (!securityService.isOwnerOrAdmin(gameTable)) {
            throw new AccessDeniedException("You do not have permission to add a time slot to this game table.");
        }

        final TimeSlot timeSlot = new TimeSlot();

        timeSlot.setGameTable(gameTable);
        timeSlot.setStartTime(request.getStartTime());
        timeSlot.setEndTime(request.getEndTime());

        timeSlotService.persist(timeSlot);

        LOG.debug("Created time slot {}.", timeSlot);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(timeSlot.getId()));
    }

    /**
     * Creates time slots in bulk for all game tables of a venue.
     */
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    @PostMapping(value = "/bulk", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createBulkTimeSlot(@RequestBody TimeSlotBulkRequest request) {
        final Venue venue = venueService.find(request.getVenueId());

        if (!securityService.isOwnerOrAdmin(venue)) {
            throw new AccessDeniedException("You do not have permission to add a time slots to this venue.");
        }

        for (GameTable gameTable : venue.getGameTables()) {
            final TimeSlot timeSlot = new TimeSlot();

            timeSlot.setGameTable(gameTable);
            timeSlot.setStartTime(request.getStartTime());
            timeSlot.setEndTime(request.getEndTime());

            timeSlotService.persist(timeSlot);

            LOG.debug("Created time slot {}.", timeSlot);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(venue.getId(), VenueController.BASE_URL));
    }

    /**
     * Updates an existing time slot.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@timeSlotService.find(#id))")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateTimeSlot(@PathVariable Integer id, @RequestBody TimeSlotRequest request) {
        final TimeSlot original = timeSlotService.find(id);

        original.setGameTable(gameTableService.find(request.getGameTableId()));
        original.setStartTime(request.getStartTime());
        original.setEndTime(request.getEndTime());

        timeSlotService.update(original);

        LOG.debug("Updated time slot {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Deletes a time slot.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@timeSlotService.find(#id))")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTimeSlot(@PathVariable Integer id) {
        final TimeSlot timeSlot = timeSlotService.find(id);

        timeSlotService.remove(timeSlot);

        LOG.debug("Removed time slot {}.", timeSlot);
    }
}
