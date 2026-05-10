package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

public class GameTableRequest {
    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Capacity must not be null")
    private Integer capacity;

    @NotNull(message = "Venue ID must not be null")
    private Integer venueId;

    public GameTableRequest(String name, Integer capacity, Integer venueId) {
        this.name = name;
        this.capacity = capacity;
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Integer getVenueId() {
        return venueId;
    }
}
