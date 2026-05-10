package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class TimeSlotBulkRequest {
    @NotNull(message = "Start time must not be null")
    private LocalTime startTime;

    @NotNull(message = "End time must not be null")
    private LocalTime endTime;

    @NotNull(message = "Venue ID must not be null")
    private Integer venueId;

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Integer getVenueId() {
        return venueId;
    }
}
