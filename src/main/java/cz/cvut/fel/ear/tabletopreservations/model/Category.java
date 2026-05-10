package cz.cvut.fel.ear.tabletopreservations.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category used to classify desk games.
 */
@Entity
@Table(name = "category")
public class Category extends AbstractEntity {

    /**
     * Attributes
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Relations
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "categories")
    private List<DeskGame> deskGames;

    /**
     * Getters and Setters
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeskGame> getDeskGames() {
        return deskGames;
    }

    public void setDeskGames(List<DeskGame> deskGames) {
        this.deskGames = deskGames;
    }

    public void addDeskGame(DeskGame deskGame) {
        if (this.deskGames == null) {
            this.deskGames = new ArrayList<>();
        }

        this.deskGames.add(deskGame);
        deskGame.addCategory(this);
    }

    public void removeDeskGame(DeskGame deskGame) {
        if (this.deskGames == null) {
            return;
        }

        this.deskGames.remove(deskGame);
        deskGame.removeCategory(this);
    }
}
