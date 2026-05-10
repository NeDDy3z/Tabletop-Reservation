package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.*;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a physical table at a venue that can be reserved for games.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "GameTable.findByTimeSlot",
        query = "SELECT gt FROM GameTable gt " +
                "JOIN gt.timeSlots ts " +
                "WHERE ts = :timeSlot"
    ),
    @NamedQuery(
        name = "GameTable.findAllByReservation",
        query = "SELECT DISTINCT ts.gameTable FROM Reservation r " +
                "JOIN r.timeSlots ts " +
                "WHERE r = :reservation"
    )
})
@Table(name = "game_table")
public class GameTable extends AbstractEntity implements Ownable {

    /**
     * Attributes
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    /**
     * Relations
     */
    @ManyToOne(optional = false)
    private Venue venue;

    @OneToMany(mappedBy = "gameTable", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("startTime ASC")
    private List<TimeSlot> timeSlots;

    /**
     * Getters and setters
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        if (this.timeSlots == null) {
            this.timeSlots = new ArrayList<>();
        }
        this.timeSlots.add(timeSlot);
    }

    public void removeTimeSlot(TimeSlot timeSlot) {
        if (this.timeSlots == null) {
            return;
        }
        this.timeSlots.remove(timeSlot);
    }

    @Override
    public User getOwner() {
        return venue != null ? venue.getOwner() : null;
    }
}
