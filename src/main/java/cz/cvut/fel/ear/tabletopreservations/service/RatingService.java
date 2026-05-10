package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.exception.ValidationException;
import cz.cvut.fel.ear.tabletopreservations.model.*;
import cz.cvut.fel.ear.tabletopreservations.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for creating, retrieving and managing user/venue ratings.
 */
@Service
@Transactional
public class RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Returns all ratings.
     */
    @Transactional(readOnly = true)
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    /**
     * Finds a rating by its composite ID.
     */
    @Transactional(readOnly = true)
    public Rating find(RatingId id) {
        Objects.requireNonNull(id);
        Rating rating = ratingRepository.find(id);
        if (rating == null) {
            throw NotFoundException.create("Rating", id);
        }
        return rating;
    }

    /**
     * Returns ratings created by the given user.
     */
    @Transactional(readOnly = true)
    public List<Rating> findByRater(User rater) {
        Objects.requireNonNull(rater);
        return ratingRepository.findByRater(rater);
    }

    /**
     * Returns ratings received by the given user.
     */
    @Transactional(readOnly = true)
    public List<Rating> findByRated(User rated) {
        Objects.requireNonNull(rated);
        return ratingRepository.findByRated(rated);
    }

    /**
     * Returns ratings associated with the given venue.
     */
    @Transactional(readOnly = true)
    public List<Rating> findByVenue(Venue venue) {
        Objects.requireNonNull(venue);
        return ratingRepository.findByVenue(venue);
    }

    /**
     * Creates a new rating for the given reservation participant.
     */
    public void createRating(User rater, User rated, Reservation reservation, int score, String comment) {
        Objects.requireNonNull(rater);
        Objects.requireNonNull(rated);
        Objects.requireNonNull(reservation);

        if (score < 1 || score > 5) {
            throw new ValidationException("Score must be between 1 and 5");
        }
        if (rater.getId().equals(rated.getId())) {
            throw new ValidationException("User cannot rate themselves");
        }

        Rating rating = new Rating(rater, rated, reservation);
        rating.setScore(score);
        rating.setComment(comment);

        ratingRepository.persist(rating);
    }

    /**
     * Updates an existing rating (validates score range).
     */
    public void update(Rating rating) {
        Objects.requireNonNull(rating);
        if (rating.getScore() < 1 || rating.getScore() > 5) {
            throw new ValidationException("Score must be between 1 and 5");
        }
        ratingRepository.update(rating);
    }

    /**
     * Removes a rating.
     */
    public void remove(Rating rating) {
        Objects.requireNonNull(rating);
        ratingRepository.remove(rating);
    }
}
