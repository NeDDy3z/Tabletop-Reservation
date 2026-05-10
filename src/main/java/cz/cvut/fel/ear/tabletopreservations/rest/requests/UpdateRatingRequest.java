package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for rating update requests.
 * Ids are included in the request path.
 */
public class UpdateRatingRequest {
    @NotNull(message = "Score must not be null")
    private int score;

    @NotNull(message = "Comment must not be null")
    private String comment;


    public int getScore() {
        return score;
    }

    public String getComment() {
        return comment;
    }
}
