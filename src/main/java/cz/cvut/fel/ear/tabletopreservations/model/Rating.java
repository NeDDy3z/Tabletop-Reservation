package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.io.Serializable;

/**
 * Represents a rating given by one user to another for a reservation.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Rating.findByUsers",
        query = "SELECT r FROM Rating r " +
                "WHERE r.rater = :rater AND r.rated = :rated"
    ),
    @NamedQuery(
        name = "Rating.findByRater",
        query = "SELECT r FROM Rating r WHERE r.rater = :rater"
    ),
    @NamedQuery(
        name = "Rating.findByRated",
        query = "SELECT r FROM Rating r WHERE r.rated = :rated"
    ),
    @NamedQuery(
        name = "Rating.findByVenue",
        query = "SELECT DISTINCT r FROM Rating r " +
                "JOIN r.reservation res " +
                "JOIN res.timeSlots ts " +
                "WHERE ts.gameTable.venue = :venue"
    ),
    @NamedQuery(
        name = "Rating.findAll",
        query = "SELECT r FROM Rating r"
    ),
    @NamedQuery(
        name = "Rating.findAverageForUser",
        query = "SELECT AVG(r.score) FROM Rating r WHERE r.rated = :user"
    ),
    @NamedQuery(
        name = "Rating.countForUser",
        query = "SELECT COUNT(r) FROM Rating r WHERE r.rated = :user"
    ),
    @NamedQuery(
        name = "Rating.countByUser",
        query = "SELECT COUNT(r) FROM Rating r WHERE r.rater = :user"
    )
})
@Table(name = "rating")
public class Rating implements Serializable {

    /**
     * Composite Primary Key
     */
    @EmbeddedId
    private RatingId id;

    /**
     * Attributes
     */
    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "comment", nullable = true, length = 1000)
    private String comment;

    /**
     * Relations
     */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("raterId")
    @JoinColumn(name = "rating_user_id", nullable = false)
    private User rater;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("ratedId")
    @JoinColumn(name = "rated_user_id", nullable = false)
    private User rated;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @MapsId("reservationId")
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    /**
     * Constructors
     */
    public Rating() {
        this.id = new RatingId();
    }

    public Rating(User rater, User rated, Reservation reservation) {
        this.id = new RatingId(rater.getId(), rated.getId(), reservation.getId());
        this.rater = rater;
        this.rated = rated;
        this.reservation = reservation;
    }

    /**
     * Getters and Setters
     */
    public RatingId getId() {
        return id;
    }

    public void setId(RatingId id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getRater() {
        return rater;
    }

    public void setRater(User rater) {
        this.rater = rater;
        if (this.id == null) {
            this.id = new RatingId();
        }
        this.id.setRaterId(rater.getId());
    }

    public User getRated() {
        return rated;
    }

    public void setRated(User rated) {
        this.rated = rated;
        if (this.id == null) {
            this.id = new RatingId();
        }
        this.id.setRatedId(rated.getId());
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        if (this.id == null) {
            this.id = new RatingId();
        }
        this.id.setReservationId(reservation.getId());
    }
}
