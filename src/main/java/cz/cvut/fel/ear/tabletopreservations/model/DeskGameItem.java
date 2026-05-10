package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.*;

/**
 * Represents a concrete instance of a desk game owned by a venue.
 */
@Entity
@Table(name = "desk_game_item")
public class DeskGameItem extends AbstractEntity {

    /**
     * Attributes
     */
    @Basic(optional = false)
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * Relations
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "desk_game_id", nullable = false)
    private DeskGame deskGame;

    @ManyToOne(optional = false)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    /**
     * Getters and setters
     */
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public DeskGame getDeskGame() {
        return deskGame;
    }

    public void setDeskGame(DeskGame deskGame) {
        this.deskGame = deskGame;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }
}
