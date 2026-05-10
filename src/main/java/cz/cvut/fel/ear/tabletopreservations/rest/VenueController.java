package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.DeskGameItem;
import cz.cvut.fel.ear.tabletopreservations.model.GameTable;
import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.GameTableResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.DeskGameItemRequest;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.GameTableRequest;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.VenueRequest;
import cz.cvut.fel.ear.tabletopreservations.security.SecurityService;
import cz.cvut.fel.ear.tabletopreservations.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for venue management.
 */
@RestController
@RequestMapping(VenueController.BASE_URL)
public class VenueController {

    public static final String BASE_URL = "/rest/venues";

    private static final Logger LOG = LoggerFactory.getLogger(VenueController.class);

    private final DeskGameItemService deskGameItemService;
    private final DeskGameService deskGameService;
    private final GameTableService gameTableService;
    private final SecurityService securityService;
    private final TimeSlotService timeSlotService;
    private final VenueService venueService;


    @Autowired
    public VenueController(
            DeskGameItemService deskGameItemService,
            DeskGameService deskGameService,
            GameTableService gameTableService,
            SecurityService securityService,
            TimeSlotService timeSlotService,
            VenueService venueService
    ) {
        this.deskGameItemService = deskGameItemService;
        this.deskGameService = deskGameService;
        this.gameTableService = gameTableService;
        this.securityService = securityService;
        this.timeSlotService = timeSlotService;
        this.venueService = venueService;
    }

    /**
     * Returns all venues.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Venue> getVenues() {
        return venueService.findAll();
    }

    /**
     * Returns venue details by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Venue getVenue(@PathVariable Integer id) {
        return venueService.find(id);
    }

    /**
     * Searches venues by city.
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Venue> searchByCity(@RequestParam String city) {
        return venueService.findByCity(city);
    }

    /**
     * Creates a new venue.
     */
    @PreAuthorize("hasRole('ROLE_PROVIDER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createVenue(@RequestBody VenueRequest request) {
        Venue venue = new Venue();
        venue.setName(request.getName());
        venue.setStreet(request.getStreet());
        venue.setCity(request.getCity());
        venue.setPostalCode(request.getPostalCode());
        venue.setCountry(request.getCountry());
        venue.setOwner(securityService.getCurrentUser());

        venueService.persist(venue);

        LOG.debug("Created venue {}.", venue);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(venue.getId()));
    }

    /**
     * Updates venue details.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateVenue(@PathVariable Integer id, @RequestBody VenueRequest request) {
        final Venue original = venueService.find(id);

        venueService.updateVenue(original, request);

        LOG.debug("Updated venue {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Deletes a venue.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVenue(@PathVariable Integer id) {
        final Venue venue = venueService.find(id);

        venueService.remove(venue);

        LOG.debug("Removed venue {}.", venue);
    }

    /**
     * Returns game tables assigned to the venue.
     */
    @GetMapping(value = "/{id}/gametables", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GameTableResponse> getGameTables(@PathVariable Integer id) {
        final Venue venue = venueService.find(id);

        return venue.getGameTables().stream()
                .map(GameTableResponse::fromGameTable)
                .toList();
    }

    /**
     * Assigns existing game tables to the venue.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @PutMapping(value = "/{id}/gametables", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> assignGameTable(@PathVariable Integer id, @RequestBody List<Integer> gameTableIds) {
        final Venue venue = venueService.find(id);

        for (Integer gameTableId : gameTableIds) {
            final GameTable gameTable = gameTableService.find(gameTableId);

            if (securityService.isOwnerOrAdmin(gameTable)) {
                venueService.addGameTable(venue, gameTable);
            } else {
                throw new SecurityException("You do not have permission to assign this game table.");
            }

            LOG.debug("Assigned game table {} to venue {}.", gameTable, venue);
        }

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(venue.getId()));
    }

    /**
     * Removes a game table from the venue.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @DeleteMapping(value = "/{id}/gametables/{gameTableId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGameTable(@PathVariable Integer id, @PathVariable Integer gameTableId) {
        final Venue venue = venueService.find(id);
        final GameTable gameTable = gameTableService.find(gameTableId);

        venueService.removeGameTable(venue, gameTable);

        LOG.debug("Removed game table {} from venue {}.", gameTable, venue);
    }

    /**
     * Returns all time slots for the venue.
     */
    @GetMapping(value = "/{id}/timeslots", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimeSlot> getTimeSlots(@PathVariable Integer id) {
        final Venue venue = venueService.find(id);
        return venue.getTimeSlots();
    }

    /**
     * Returns available time slots for the venue on a given date.
     */
    @GetMapping(value = "/{id}/timeslots/available", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TimeSlot> getAvailableTimeSlots(@PathVariable Integer id, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        final Venue venue = venueService.find(id);

        if (date == null) {
            date = LocalDate.now();
        }

        return timeSlotService.findFreeByVenueAndDate(venue, date);
    }

    /**
     * Returns all desk game items available at the venue.
     */
    @GetMapping(value = "/{id}/deskgames", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeskGameItem> getDeskGameItems(@PathVariable Integer id) {
        final Venue venue = venueService.find(id);
        return venue.getDeskGameItems();
    }

    /**
     * Returns a specific desk game item at the venue.
     */
    @GetMapping(value = "/{id}/deskgames/{deskGameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeskGameItem getDeskGameItem(@PathVariable Integer id, @PathVariable Integer deskGameId) {
        final Venue venue = venueService.find(id);
        return venue.getDeskGameItemById(deskGameId);
    }

    /**
     * Adds a new desk game item to the venue.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @PostMapping(value = "/{id}/deskgames", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> addDeskGameItem(@PathVariable Integer id, @RequestBody DeskGameItemRequest request) {
        final Venue venue = venueService.find(id);

        DeskGameItem item = new DeskGameItem();
        item.setDeskGame(deskGameService.find(request.getDeskGameId()));
        item.setQuantity(request.getQuantity());

        venueService.addDeskGameItem(venue, item);

        LOG.debug("Added desk game item {} to venue {}.", item, venue);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(item.getId(), "/rest/deskgameitems"));
    }

    /**
     * Updates quantity of a desk game item.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @PostMapping(value = "/{id}/deskgames/{deskGameItemId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateDeskGameItem(@PathVariable Integer id, @RequestBody DeskGameItemRequest request) {
        final Venue venue = venueService.find(id);

        DeskGameItem item = venue.getDeskGameItemById(request.getDeskGameId());
        item.setQuantity(request.getQuantity());
        venueService.update(venue);

        LOG.debug("Updated desk game items {} to venue {}.", item, venue);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(item.getId(), "/rest/deskgameitems"));
    }

    /**
     * Removes a desk game item from the venue.
     */
    @PreAuthorize("@securityService.isOwnerOrAdmin(@venueService.find(#id))")
    @DeleteMapping(value = "/{id}/deskgames/{deskGameItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeDeskGameItem(@PathVariable Integer id, @PathVariable Integer deskGameItemId) {
        final Venue venue = venueService.find(id);
        final DeskGameItem deskGameItem = deskGameItemService.find(deskGameItemId);

        venueService.removeDeskGameItem(venue, deskGameItem);

        LOG.debug("Removed desk game item {} from venue {}.", deskGameItem, venue);
    }
}
