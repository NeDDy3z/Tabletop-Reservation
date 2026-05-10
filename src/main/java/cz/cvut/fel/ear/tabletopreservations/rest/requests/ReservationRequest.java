package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for reservation creation requests.
 */
public class ReservationRequest {
    @NotNull(message = "Date must not be null")
    private LocalDate date;

    private List<Integer> timeSlotIds;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<Integer> getTimeSlotIds() {
        return timeSlotIds;
    }

    public void setTimeSlotIds(List<Integer> timeSlotIds) {
        this.timeSlotIds = timeSlotIds;
    }
}

