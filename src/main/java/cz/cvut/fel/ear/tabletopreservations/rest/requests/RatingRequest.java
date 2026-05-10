package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for rating creation requests.
 */
public class RatingRequest {

    @NotNull(message = "Rater ID must not be null")
    private Integer raterId;

    @NotNull(message = "Rated ID must not be null")
    private Integer ratedId;

    @NotNull(message = "Reservation ID must not be null")
    private Integer reservationId;

    @NotNull(message = "Score must not be null")
    private int score;

    @NotNull(message = "Comment must not be null")
    private String comment;

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
}