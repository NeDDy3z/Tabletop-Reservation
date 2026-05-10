package cz.cvut.fel.ear.tabletopreservations.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a board game available in the system.
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "DeskGame.findByMaxPlayers",
        query = "SELECT dg FROM DeskGame dg " +
                "WHERE dg.maxPlayers >= :maxPlayers"
    ),
    @NamedQuery(
        name = "DeskGame.findByAveragePlaytimeMinutes",
        query = "SELECT dg FROM DeskGame dg " +
                "WHERE dg.averagePlaytimeMinutes = :playtime"
    ),
    @NamedQuery(
        name = "DeskGame.findByMinAge",
        query = "SELECT dg FROM DeskGame dg " +
                "WHERE dg.minAge <= :age"
    ),
    @NamedQuery(
        name = "DeskGame.findByCategory",
        query = "SELECT DISTINCT dg FROM DeskGame dg " +
                "JOIN dg.categories c " +
                "WHERE c = :category"
    )
})
@Table(name = "desk_game")
public class DeskGame extends AbstractEntity {

    /**
     * Attributes
     */
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "min_age", nullable = false)
    private int minAge;

    @Column(name = "min_players", nullable = false)
    private int minPlayers;

    @Column(name = "max_players", nullable = false)
    private int maxPlayers;

    @Column(name = "average_playtime_minutes", nullable = false)
    private int averagePlaytimeMinutes;

    /**
     * Relations
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "desk_game_category",
            joinColumns = @JoinColumn(name = "desk_game_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private List<Category> categories;

    /**
     * Getters and Setters
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getAveragePlaytimeMinutes() {
        return averagePlaytimeMinutes;
    }

    public void setAveragePlaytimeMinutes(int averagePlaytimeMinutes) {
        this.averagePlaytimeMinutes = averagePlaytimeMinutes;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category) {
        if (this.categories == null) {
            this.categories = new ArrayList<>();
        }

        this.categories.add(category);
        category.addDeskGame(this);
    }

    public void removeCategory(Category category) {
        if (this.categories == null) {
            return;
        }

        this.categories.remove(category);
        category.removeDeskGame(this);
    }
}
