package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class TimeSlotRequest {
    @NotNull(message = "Start time must not be null")
    private LocalTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalTime endTime;

    @NotNull(message = "Game table ID must not be null")
    private Integer gameTableId;

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getGameTableId() {
        return gameTableId;
    }
}
