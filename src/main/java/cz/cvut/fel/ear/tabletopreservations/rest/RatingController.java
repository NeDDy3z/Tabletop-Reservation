package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.Rating;
import cz.cvut.fel.ear.tabletopreservations.model.RatingId;
import cz.cvut.fel.ear.tabletopreservations.model.Reservation;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.RatingResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.RatingRequest;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.UpdateRatingRequest;
import cz.cvut.fel.ear.tabletopreservations.security.SecurityService;
import cz.cvut.fel.ear.tabletopreservations.service.RatingService;
import cz.cvut.fel.ear.tabletopreservations.service.ReservationService;
import cz.cvut.fel.ear.tabletopreservations.service.UserService;
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
 * REST controller for managing user ratings.
 */
@RestController
@RequestMapping("/rest/ratings")
public class RatingController {

    private static final Logger LOG = LoggerFactory.getLogger(RatingController.class);

    private final RatingService ratingService;
    private final UserService userService;
    private final SecurityService securityService;
    private final ReservationService reservationService;
    private final VenueService venueService;

    @Autowired
    public RatingController(
            RatingService ratingService,
            UserService userService,
            SecurityService securityService,
            ReservationService reservationService,
            VenueService venueService
    ) {
        this.ratingService = ratingService;
        this.userService = userService;
        this.securityService = securityService;
        this.reservationService = reservationService;
        this.venueService = venueService;
    }


    /**
     * Returns all ratings received by the given user.
     */
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @GetMapping(value = "/{ratedId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingResponse> getRatingsByRatedUser(@PathVariable Integer ratedId) {
        List<Rating> ratings = ratingService.findByRated(userService.find(ratedId));

        return ratings.stream()
                .map(RatingResponse::fromRating)
                .toList();
    }

    /**
     * Returns all ratings created by the currently authenticated user.
     */
    @PreAuthorize("hasRole('ROLE_PLAYER')")
    @GetMapping(value = "/my", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingResponse> getRatingsByRaterUser() {
        List<Rating> ratings = ratingService.findByRater(securityService.getCurrentUser());

        return ratings.stream()
                .map(RatingResponse::fromRating)
                .toList();
    }

    /**
     * Returns all ratings related to the given venue.
     */
    @GetMapping(value = "/venue/{venueId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RatingResponse> getRatingsByVenue(@PathVariable Integer venueId) {
        List<Rating> ratings = ratingService.findByVenue(venueService.find(venueId));

        return ratings.stream()
                .map(RatingResponse::fromRating)
                .toList();
    }

    /**
     * Returns a specific rating identified by rater, rated user and reservation.
     */
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') || " +
                    "(" +
                    "@securityService.getCurrentUser().getId() == #raterId || " +
                    "@securityService.getCurrentUser().getId() == #ratedId" +
                    ")"
    )
    @GetMapping(value = "/{raterId}/{ratedId}/{reservationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RatingResponse getRating(@PathVariable Integer raterId, @PathVariable Integer ratedId, @PathVariable Integer reservationId) {
        RatingId id = new RatingId(raterId, ratedId, reservationId);

        return RatingResponse.fromRating(ratingService.find(id));
    }

    /**
     * Creates a new rating for a reservation.
     */
    @PreAuthorize("hasAnyRole('ROLE_PLAYER', 'ROLE_PROVIDER', 'ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createRating(@RequestBody RatingRequest request) {
        final User rater = userService.find(request.getRaterId());
        final User rated = userService.find(request.getRatedId());
        final Reservation reservation = reservationService.find(request.getReservationId());

        if (rater.getId().equals(rated.getId())) {
            throw new IllegalArgumentException("Users are not allowed to rate themselves");
        }

        if (!securityService.getCurrentUser().getId().equals(rater.getId())) {
            throw new AccessDeniedException("Users are not allowed to create ratings on behalf of other users");
        }

        if (!reservation.isParticipant(rater) || !reservation.isParticipant(rated)) {
            throw new IllegalArgumentException("Both rater and rated users must be participants of the reservation");
        }

        ratingService.createRating(rater, rated, reservation, request.getScore(), request.getComment());

        LOG.debug("Created rating from user {} to user {} with score {}.", rater.getId(), rated.getId(), request.getScore());

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(rated.getId(), "/rest/rating/" + rater.getId()));
    }

    /**
     * Updates an existing rating.
     */
    // Business note: Only admins can update ratings to ensure integrity.
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{raterId}/{ratedId}/{reservationId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateRating(
            @PathVariable Integer raterId,
            @PathVariable Integer ratedId,
            @PathVariable Integer reservationId,
            @RequestBody UpdateRatingRequest request
    ) {
        RatingId id = new RatingId(raterId, ratedId, reservationId);
        final Rating rating = ratingService.find(id);

        rating.setScore(request.getScore());
        rating.setComment(request.getComment());

        ratingService.update(rating);

        LOG.debug("Updated rating {}.", rating);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(rating.getRated().getId(), "/rest/rating/" + rating.getRater().getId()));
    }

    /**
     * Deletes an existing rating.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') || #raterId == @securityService.getCurrentUser().getId()")
    @DeleteMapping(value = "/{raterId}/{ratedId}/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRating(
            @PathVariable Integer raterId,
            @PathVariable Integer ratedId,
            @PathVariable Integer reservationId
    ) {
        RatingId id = new RatingId(raterId, ratedId, reservationId);
        final Rating rating = ratingService.find(id);

        ratingService.remove(rating);

        LOG.debug("Removed rating {}.", rating);
    }
}
