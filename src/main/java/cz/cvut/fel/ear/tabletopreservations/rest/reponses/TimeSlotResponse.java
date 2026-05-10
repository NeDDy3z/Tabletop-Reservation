package cz.cvut.fel.ear.tabletopreservations.rest.reponses;

import cz.cvut.fel.ear.tabletopreservations.model.TimeSlot;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeSlotResponse {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeSlotResponse(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static TimeSlotResponse fromTimeSlot(TimeSlot timeSlot) {
        return new TimeSlotResponse(timeSlot.getStartTime(), timeSlot.getEndTime());
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}

