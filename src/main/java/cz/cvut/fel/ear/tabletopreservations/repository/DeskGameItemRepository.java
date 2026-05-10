package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.DeskGame;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGameItem;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for DeskGameItem entities.
 *
 * Handles queries for physical game availability at venues.
 */
@Repository
public class DeskGameItemRepository extends BaseRepository<DeskGameItem> {

    public DeskGameItemRepository() {
        super(DeskGameItem.class);
    }

    /**
     * Finds all desk game items available at the given venue.
     */
    public List<DeskGameItem> findByVenue(Venue venue) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(DeskGameItem.class);
        var root = cq.from(DeskGameItem.class);

        cq.select(root)
                .where(cb.equal(root.get("venue"), venue));

        return em.createQuery(cq).getResultList();
    }

    /**
     * Finds all desk game items representing the given desk game.
     */
    public List<DeskGameItem> findByDeskGame(DeskGame deskGame) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(DeskGameItem.class);
        var root = cq.from(DeskGameItem.class);

        cq.select(root)
                .where(cb.equal(root.get("deskGame"), deskGame));

        return em.createQuery(cq).getResultList();
    }
}
