package cz.cvut.fel.ear.tabletopreservations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.List;

/**
 * Represents a reservable time interval for a game table.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "TimeSlot.findByReservation",
        query = "SELECT ts FROM Reservation r JOIN r.timeSlots ts WHERE r = :reservation"
    ),
    @NamedQuery(
        name = "TimeSlot.findFreeByGameTableAndDate",
        query = "SELECT ts FROM TimeSlot ts " +
                    "WHERE ts.gameTable = :table " +
                    "AND NOT EXISTS (" +
                    "   SELECT 1 FROM Reservation r " +
                    "   JOIN r.timeSlots rts " +
                    "   WHERE rts = ts " +
                    "     AND r.date = :date " +
                    "     AND r.status IN (:activeStates)" +
                    ")"
    ),
    @NamedQuery(
        name = "TimeSlot.findFreeByVenueAndDate",
        query = "SELECT ts FROM TimeSlot ts " +
                    "WHERE ts.gameTable.venue = :venue " +
                    "AND NOT EXISTS (" +
                    "   SELECT 1 FROM Reservation r " +
                    "   JOIN r.timeSlots rts " +
                    "   WHERE rts = ts " +
                    "     AND r.date = :date " +
                    "     AND r.status IN (:activeStates)" +
                    ")"
    )
})
@Table(name = "time_slot")
public class TimeSlot extends AbstractEntity implements Ownable {

    /**
     * Attributes
     */
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    /**
     * Relations
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "timeSlots", fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "game_table_id", nullable = false)
    private GameTable gameTable;

    /**
     * Getters and setters
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new java.util.ArrayList<>();
        }
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        if (this.reservations == null) {
            return;
        }
        this.reservations.remove(reservation);
    }

    public GameTable getGameTable() {
        return gameTable;
    }

    public void setGameTable(GameTable gameTable) {
        this.gameTable = gameTable;
    }

    @Override
    public User getOwner() {
        return this.gameTable.getOwner();
    }
}
