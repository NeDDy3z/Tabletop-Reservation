package cz.cvut.fel.ear.tabletopreservations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user of the reservation system.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "User.findByEmail",
        query = "SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)"
    ),
    @NamedQuery(
        name = "User.findByFirstName",
        query = "SELECT u FROM User u WHERE LOWER(u.firstName) = LOWER(:firstName)"
    ),
    @NamedQuery(
        name = "User.findByLastName",
        query = "SELECT u FROM User u WHERE LOWER(u.lastName) = LOWER(:lastName)"
    ),
    @NamedQuery(
        name = "User.findActiveUsers",
        query = "SELECT u FROM User u WHERE u.isActive = true"
    ),
    @NamedQuery(
        name = "User.findInactiveUsers",
        query = "SELECT u FROM User u WHERE u.isActive = false"
    ),
    @NamedQuery(
        name = "User.findByRole",
        query = "SELECT DISTINCT u FROM User u JOIN u.roles r WHERE r = :role"
    )
})
@Table(name = "tabletop_user")
public class User extends AbstractEntity {

    /**
     * Attributes
     */
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @Column(name = "lastname", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();

    /**
     * Relations
     */
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @JsonIgnore
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Venue> venues;

    @JsonIgnore
    @OneToMany(mappedBy = "rater", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> givenRatings;

    @JsonIgnore
    @OneToMany(mappedBy = "rated", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> receivedRatings;


    /**
     * Getters and setters
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        if (this.roles == null) {
            this.roles = new ArrayList<>();
        }

        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (this.roles == null) {
            return;
        }
        this.roles.remove(role);
    }

    public boolean isAdmin() {
        return roles != null && roles.contains(Role.ADMIN);
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public void addReservation(Reservation reservation) {
        if (this.reservations == null) {
            this.reservations = new ArrayList<>();
        }
        this.reservations.add(reservation);
    }

    public void removeReservation(Reservation reservation) {
        if (this.reservations == null) {
            return;
        }
        this.reservations.remove(reservation);
    }

    public List<Venue> getVenues() {
        return venues;
    }

    public void setVenues(List<Venue> venues) {
        this.venues = venues;
    }

    public void addVenue(Venue venue) {
        if (this.venues == null) {
            this.venues = new ArrayList<>();
        }
        this.venues.add(venue);
    }

    public void removeVenue(Venue venue) {
        if (this.venues == null) {
            return;
        }
        this.venues.remove(venue);
    }

    public List<Rating> getGivenRatings() {
        return givenRatings;
    }

    public void setGivenRatings(List<Rating> givenRatings) {
        this.givenRatings = givenRatings;
    }

    public void addGivenRating(Rating rating) {
        if (this.givenRatings == null) {
            this.givenRatings = new ArrayList<>();
        }
        this.givenRatings.add(rating);
    }

    public void removeGivenRating(Rating rating) {
        if (this.givenRatings == null) {
            return;
        }
        this.givenRatings.remove(rating);
    }

    public List<Rating> getReceivedRatings() {
        return receivedRatings;
    }

    public void setReceivedRatings(List<Rating> receivedRatings) {
        this.receivedRatings = receivedRatings;
    }

    public void addReceivedRating(Rating rating) {
        if (this.receivedRatings == null) {
            this.receivedRatings = new ArrayList<>();
        }
        this.receivedRatings.add(rating);
    }

    public void removeReceivedRating(Rating rating) {
        if (this.receivedRatings == null) {
            return;
        }
        this.receivedRatings.remove(rating);
    }
}
