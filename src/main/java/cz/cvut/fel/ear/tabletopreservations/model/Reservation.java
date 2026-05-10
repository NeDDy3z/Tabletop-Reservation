package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a reservation of one or more time slots by a user.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Reservation.findByUser",
        query = "SELECT r FROM Reservation r WHERE r.user = :user"
    ),
    @NamedQuery(
        name = "Reservation.findByTimeSlot",
        query = "SELECT r FROM Reservation r JOIN r.timeSlots ts WHERE ts = :timeSlot"
    ),
    @NamedQuery(
        name = "Reservation.findByGameTable",
        query = "SELECT DISTINCT r FROM Reservation r " +
                "JOIN r.timeSlots ts " +
                "WHERE ts.gameTable = :gameTable"
    ),
    @NamedQuery(
        name = "Reservation.findByStatusFromDate",
        query = "SELECT r FROM Reservation r " +
                "WHERE r.status = :status AND r.date >= :fromDate"
    )
})
@Table(name = "reservation")
public class Reservation extends AbstractEntity implements Ownable {

    /**
     * Attributes
     */
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationState status = ReservationState.WAITING_FOR_CONFIRMATION;

    /**
     * Relations
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
            name = "reservation_time_slot",
            joinColumns = @JoinColumn(name = "reservation_id"),
            inverseJoinColumns = @JoinColumn(name = "time_slot_id")
    )
    private List<TimeSlot> timeSlots;

    /**
     * Getters and setters
     */
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ReservationState getStatus() {
        return status;
    }

    public void setStatus(ReservationState status) {
        this.status = status;
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

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public User getOwner() {
        return user;
    }

    public boolean isParticipant(User user) {
        if (user == null || timeSlots == null) {
            return false;
        }

        if (this.user.equals(user)) {
            return true;
        }

        for (TimeSlot timeSlot : timeSlots) {
            if (timeSlot.getGameTable().getOwner().equals(user)) {
                return true;
            }
        }

        return false;
    }
}
