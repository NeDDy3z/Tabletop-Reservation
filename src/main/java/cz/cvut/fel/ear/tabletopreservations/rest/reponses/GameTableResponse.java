package cz.cvut.fel.ear.tabletopreservations.rest.reponses;

import cz.cvut.fel.ear.tabletopreservations.model.GameTable;

import java.io.Serializable;

public class GameTableResponse implements Serializable {
    private Integer id;
    private String name;
    private Integer capacity;

    public GameTableResponse(Integer id, String name, Integer capacity) {
        this.id = id;
        this.name = name;
    }

    public static GameTableResponse fromGameTable(GameTable gameTable) {
        return new GameTableResponse(gameTable.getId(), gameTable.getName(), gameTable.getCapacity());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

