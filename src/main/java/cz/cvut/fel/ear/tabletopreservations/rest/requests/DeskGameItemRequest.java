package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

public class DeskGameItemRequest {

    @NotNull(message = "Desk game ID must not be null")
    private Integer DeskGameId;

    @NotNull(message = "Quantity must not be null")
    private Integer quantity;

    public DeskGameItemRequest(Integer deskGameId, Integer quantity) {
        DeskGameId = deskGameId;
        this.quantity = quantity;
    }

    public Integer getDeskGameId() {
        return DeskGameId;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
