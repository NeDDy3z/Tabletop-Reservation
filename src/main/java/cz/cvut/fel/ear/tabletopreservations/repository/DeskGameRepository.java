package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.DeskGame;
import cz.cvut.fel.ear.tabletopreservations.model.Category;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Repository for managing desk game entities.
 */
@Repository
public class DeskGameRepository extends BaseRepository<DeskGame> {

    public DeskGameRepository() {
        super(DeskGame.class);
    }

    /**
     * Finds all desk games with the given title.
     */
    public List<DeskGame> findByTitle(String title) {
        var cb = em.getCriteriaBuilder();
        var cq = cb.createQuery(DeskGame.class);
        var root = cq.from(DeskGame.class);

        cq.select(root)
                .where(cb.equal(
                        cb.lower(root.get("title")),
                        cb.lower(cb.literal(title))
                ));

        List<DeskGame> result = em.createQuery(cq).getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds games playable by the given number of players or fewer.
     */
    public List<DeskGame> findByMaxPlayers(int maxPlayers) {
        List<DeskGame> result = em.createNamedQuery("DeskGame.findByMaxPlayers", DeskGame.class)
                .setParameter("maxPlayers", maxPlayers)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds games with the given average playtime.
     */
    public List<DeskGame> findByAveragePlaytimeMinutes(int playtimeMinutes) {
        List<DeskGame> result = em.createNamedQuery("DeskGame.findByAveragePlaytimeMinutes", DeskGame.class)
                .setParameter("playtime", playtimeMinutes)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds games suitable for players of the given age.
     */
    public List<DeskGame> findByMinAge(int age) {
        List<DeskGame> result = em.createNamedQuery("DeskGame.findByMinAge", DeskGame.class)
                .setParameter("age", age)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds games belonging to the given category.
     */
    public List<DeskGame> findByCategory(Category category) {
        List<DeskGame> result = em.createNamedQuery("DeskGame.findByCategory", DeskGame.class)
                .setParameter("category", category)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
