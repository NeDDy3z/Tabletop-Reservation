package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

/**
 * Composite primary key for Rating entity.
 * A rating is uniquely identified by the combination of rater and rated user.
 */
@Embeddable
public class RatingId implements Serializable {

    @Column(name = "rating_user_id", nullable = false)
    private Integer raterId;

    @Column(name = "rated_user_id", nullable = false)
    private Integer ratedId;

    @Column(name = "reservation_id", nullable = false)
    private Integer reservationId;

    public RatingId() {
    }

    public RatingId(Integer raterId, Integer ratedId, Integer reservationId) {
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.reservationId = reservationId;
    }

    public Integer getRaterId() {
        return raterId;
    }

    public void setRaterId(Integer raterId) {
        this.raterId = raterId;
    }

    public Integer getRatedId() {
        return ratedId;
    }

    public void setRatedId(Integer ratedId) {
        this.ratedId = ratedId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingId ratingId = (RatingId) o;
        return Objects.equals(raterId, ratingId.raterId)
                && Objects.equals(ratedId, ratingId.ratedId)
                && Objects.equals(reservationId, ratingId.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raterId, ratedId, reservationId);
    }
}

