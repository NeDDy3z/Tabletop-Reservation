package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.Rating;
import cz.cvut.fel.ear.tabletopreservations.model.RatingId;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Repository for managing Rating entities.
 */
@Repository
public class RatingRepository {

    @PersistenceContext
    protected EntityManager em;

    /**
     * Finds a rating by its composite identifier.
     */
    public Rating find(RatingId id) {
        Objects.requireNonNull(id);
        return em.find(Rating.class, id);
    }

    /**
     * Finds all ratings given by the specified user.
     */
    public List<Rating> findByRater(User rater) {
        List<Rating> result = em.createNamedQuery("Rating.findByRater", Rating.class)
                .setParameter("rater", rater)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all ratings received by the specified user.
     */
    public List<Rating> findByRated(User rated) {
        List<Rating> result = em.createNamedQuery("Rating.findByRated", Rating.class)
                .setParameter("rated", rated)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all ratings related to the given venue.
     */
    public List<Rating> findByVenue(Venue venue) {
        List<Rating> result = em.createNamedQuery("Rating.findByVenue", Rating.class)
                .setParameter("venue", venue)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all ratings.
     */
    public List<Rating> findAll() {
        try {
            List<Rating> result = em.createNamedQuery("Rating.findAll", Rating.class)
                    .getResultList();
            return result.isEmpty() ? Collections.emptyList() : result;
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Persists a new rating.
     */
    public void persist(Rating entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
            em.flush();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Updates an existing rating.
     */
    public Rating update(Rating entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Removes a rating.
     */
    public void remove(Rating entity) {
        Objects.requireNonNull(entity);
        try {
            Rating toRemove = em.contains(entity) ? entity : em.merge(entity);
            em.remove(toRemove);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Computes the average rating received by the given user.
     */
    public Double findAverageRatingForUser(User user) {
        return em.createNamedQuery("Rating.findAverageForUser", Double.class)
                .setParameter("user", user)
                .getSingleResult();
    }

    /**
     * Counts ratings received by the given user.
     */
    public int countRatingsForUser(User user) {
        Long result = em.createNamedQuery("Rating.countForUser", Long.class)
                .setParameter("user", user)
                .getSingleResult();
        return result != null ? result.intValue() : 0;
    }

    /**
     * Counts ratings given by the given user.
     */
    public int countRatingsByUser(User user) {
        Long result = em.createNamedQuery("Rating.countByUser", Long.class)
                .setParameter("user", user)
                .getSingleResult();
        return result != null ? result.intValue() : 0;
    }
}
