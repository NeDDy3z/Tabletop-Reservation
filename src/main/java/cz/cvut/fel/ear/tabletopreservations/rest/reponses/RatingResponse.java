package cz.cvut.fel.ear.tabletopreservations.rest.reponses;

import cz.cvut.fel.ear.tabletopreservations.model.Rating;

public class RatingResponse {
    private Integer raterId;
    private Integer ratedId;
    private Integer reservationId;
    private Integer score;
    private String comment;

    public RatingResponse(Integer raterId, Integer ratedId, Integer reservationId, Integer score, String comment) {
        this.raterId = raterId;
        this.ratedId = ratedId;
        this.reservationId = reservationId;
        this.score = score;
        this.comment = comment;
    }

    public static RatingResponse fromRating(Rating rating) {
        return new RatingResponse(
                rating.getRater().getId(),
                rating.getRated().getId(),
                rating.getReservation().getId(),
                rating.getScore(),
                rating.getComment()
        );
    }

    public Integer getRaterId() {
        return raterId;
    }

    public Integer getRatedId() {
        return ratedId;
    }

    public Integer getReservationId() {
        return reservationId;
    }

    public Integer getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
