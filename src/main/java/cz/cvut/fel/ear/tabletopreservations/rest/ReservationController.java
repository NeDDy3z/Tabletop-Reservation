package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.*;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.ReservationRequest;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.security.SecurityService;
import cz.cvut.fel.ear.tabletopreservations.service.ReservationService;
import cz.cvut.fel.ear.tabletopreservations.service.TimeSlotService;
import cz.cvut.fel.ear.tabletopreservations.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing reservations.
 */
@RestController
@RequestMapping("/rest/reservations")
public class ReservationController {

    private static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);

    private final ReservationService reservationService;
    private final UserService userService;
    private final TimeSlotService timeSlotService;
    private final SecurityService securityService;

    @Autowired
    public ReservationController(
            ReservationService reservationService,
            UserService userService,
            TimeSlotService timeSlotService,
            SecurityService securityService
    ) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.timeSlotService = timeSlotService;
        this.securityService = securityService;
    }

    /**
     * Returns all reservations (admin only).
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reservation> getAllReservations() {
        return reservationService.findAll();
    }

    /**
     * Returns a specific reservation if the user is authorized.
     */
    @PreAuthorize(
            "@securityService.isOwnerOrAdmin(@reservationService.find(#id)) || " +
                    "@securityService.isReservationProvider(@reservationService.find(#id))"
    )
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Reservation getReservation(@PathVariable Integer id) {
        return reservationService.find(id);
    }

    /**
     * Returns all reservations of the given user.
     */
    @PreAuthorize("@securityService.isSelfOrAdmin(#userId)")
    @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reservation> getReservationsByUser(@PathVariable Integer userId) {
        final User user = userService.find(userId);
        return user.getReservations();
    }

    /**
     * Creates a new reservation for the current user.
     */
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createReservation(@RequestBody ReservationRequest request) {
        final User user = securityService.getCurrentUser();
        final List<TimeSlot> timeSlots = new ArrayList<>();

        for (Integer slotId : request.getTimeSlotIds()) {
            timeSlots.add(timeSlotService.find(slotId));
        }

        final Reservation reservation = reservationService.createReservation(user, request.getDate(), timeSlots);

        LOG.debug("Created reservation {}.", reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(reservation.getId()));
    }

    /**
     * Confirms a reservation (provider or admin).
     */
    @PreAuthorize("@securityService.isReservationProvider(@reservationService.find(#id))")
    @PutMapping(value = "/{id}/confirm")
    public ResponseEntity<UpdatedResponse> confirmReservation(@PathVariable Integer id) {
        final User user = securityService.getCurrentUser();
        final Reservation reservation = reservationService.find(id);

        if (!user.isAdmin() && reservation.getStatus().ordinal() > 1) {
            throw new IllegalStateException("Reservation is already finalized or in next stage.");
        }

        reservationService.confirmReservation(reservation);

        LOG.debug("Confirmed reservation {}.", reservation);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(reservation.getId()));
    }

    /**
     * Cancels a reservation.
     */
    @PreAuthorize(
            "@securityService.isOwnerOrAdmin(@reservationService.find(#id)) || " +
                    "@securityService.isReservationProvider(@reservationService.find(#id))"
    )
    @PutMapping(value = "/{id}/cancel")
    public ResponseEntity<UpdatedResponse> cancelReservation(@PathVariable Integer id) {
        final User user = securityService.getCurrentUser();
        final Reservation reservation = reservationService.find(id);

        if (!user.isAdmin() && reservation.getStatus().ordinal() > 2) {
            throw new IllegalStateException("Only admins can cancel finalized reservations.");
        }

        reservationService.cancelReservation(reservation, user);

        LOG.debug("Cancelled reservation {}.", reservation);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(reservation.getId()));
    }

    /**
     * Marks a reservation as successfully completed.
     */
    @PreAuthorize("@securityService.isReservationProvider(@reservationService.find(#id))")
    @PutMapping(value = "/{id}/complete")
    public ResponseEntity<UpdatedResponse> completeReservation(@PathVariable Integer id) {
        final User user = securityService.getCurrentUser();
        final Reservation reservation = reservationService.find(id);

        reservationService.completeReservation(reservation, true);

        if (!user.isAdmin() && reservation.getStatus().ordinal() > 4 && reservation.getStatus().ordinal() == 3) {
            throw new IllegalStateException("Only admins can cancel finalized reservations.");
        }

        LOG.debug("Reservation successfully completed {}.", reservation);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(reservation.getId()));
    }

    /**
     * Marks a reservation as failed (player did not arrive).
     */
    @PreAuthorize("@securityService.isReservationProvider(@reservationService.find(#id))")
    @PutMapping(value = "/{id}/failed")
    public ResponseEntity<UpdatedResponse> completeFailedReservation(@PathVariable Integer id) {
        final User user = securityService.getCurrentUser();
        final Reservation reservation = reservationService.find(id);

        reservationService.completeReservation(reservation, false);

        if (!user.isAdmin() && reservation.getStatus().ordinal() > 1 && reservation.getStatus().ordinal() < 5) {
            throw new IllegalStateException("Only admins can cancel finalized reservations.");
        }

        LOG.debug("Player failed to arrive to reservation {}.", reservation);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(reservation.getId()));
    }

    /**
     * Deletes a reservation (admin only).
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable Integer id) {
        final Reservation reservation = reservationService.find(id);

        reservationService.remove(reservation);

        LOG.debug("Removed reservation {}.", reservation);
    }
}
