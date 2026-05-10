package cz.cvut.fel.ear.tabletopreservations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a venue offering game tables and desk games.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "Venue.findByCity",
        query = "SELECT v FROM Venue v WHERE v.city = :city"
    ),
    @NamedQuery(
        name = "Venue.findByStreet",
        query = "SELECT v FROM Venue v WHERE LOWER(v.street) = LOWER(:street)"
    ),
    @NamedQuery(
        name = "Venue.findByPostalCode",
        query = "SELECT v FROM Venue v WHERE v.postalCode = :postalCode"
    ),
    @NamedQuery(
        name = "Venue.findByCountry",
        query = "SELECT v FROM Venue v WHERE LOWER(v.country) = LOWER(:country)"
    ),
    @NamedQuery(
        name = "Venue.searchByKeyword",
        query = "SELECT v FROM Venue v " +
                    "WHERE LOWER(v.name) = LOWER(:keyword) " +
                    "   OR LOWER(v.city) = LOWER(:keyword) " +
                    "   OR LOWER(v.street) = LOWER(:keyword) " +
                    "   OR LOWER(v.country) = LOWER(:keyword)"
    )
})
@Table(name = "venue")
public class Venue extends AbstractEntity implements Ownable {

    /**
     * Attributes
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Column(name = "country", nullable = false)
    private String country;

    /**
     * Relations
     */
    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameTable> gameTables;

    @JsonIgnore
    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeskGameItem> deskGameItems;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Getters and setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<GameTable> getGameTables() {
        return gameTables;
    }

    public void setGameTables(List<GameTable> gameTables) {
        this.gameTables = gameTables;
    }

    public List<DeskGameItem> getDeskGameItems() {
        return deskGameItems;
    }

    public void setDeskGameItems(List<DeskGameItem> deskGameItems) {
        this.deskGameItems = deskGameItems;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Custom getters and setters
     */
    public void addGameTable(GameTable gameTable) {
        if (this.gameTables == null) {
            this.gameTables = new ArrayList<>();
        }
        this.gameTables.add(gameTable);
    }

    public void removeGameTable(GameTable gameTable) {
        if (this.gameTables == null) {
            return;
        }
        this.gameTables.remove(gameTable);
    }

    public void addDeskGameItem(DeskGameItem deskGameItem) {
        if (this.deskGameItems == null) {
            this.deskGameItems = new ArrayList<>();
        }
        this.deskGameItems.add(deskGameItem);
    }

    public DeskGameItem getDeskGameItemById(Integer id) {
        if (this.deskGameItems != null) {
            for (DeskGameItem item : this.deskGameItems) {
                if (item.getId().equals(id)) {
                    return item;
                }
            }
        }

        return null;
    }

    public void removeDeskGameItem(DeskGameItem deskGameItem) {
        if (this.deskGameItems == null) {
            return;
        }

        this.deskGameItems.remove(deskGameItem);
    }

    public List<TimeSlot> getTimeSlots() {
        List<TimeSlot> timeSlots = new ArrayList<>();

        if (this.gameTables != null) {
            for (GameTable gameTable : this.gameTables) {
                if (gameTable.getTimeSlots() != null) {
                    timeSlots.addAll(gameTable.getTimeSlots());
                }
            }
        }

        return timeSlots;
    }

    public void addTimeSlotToAllGameTables(TimeSlot timeSlot) {
        Objects.requireNonNull(timeSlot, "TimeSlot must not be null");

        if (this.gameTables != null) {
            for (GameTable gameTable : this.gameTables) {
                gameTable.addTimeSlot(timeSlot);
            }
        }
    }
}
