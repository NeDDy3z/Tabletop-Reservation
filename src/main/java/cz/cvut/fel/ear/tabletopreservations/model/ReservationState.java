package cz.cvut.fel.ear.tabletopreservations.model;

/**
 * Defines possible states of a reservation.
 */
public enum ReservationState {
    WAITING_FOR_CONFIRMATION("WAITING_FOR_CONFIRMATION"),
    CONFIRMED("CONFIRMED"),
    CANCELED_BY_PLAYER("CANCELED_BY_PLAYER"),
    REJECTED("REJECTED"),
    COMPLETED("COMPLETED"),
    FAILED_TO_ARRIVE("FAILED_TO_ARRIVE");

    private final String name;

    ReservationState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }


}
