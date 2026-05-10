package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.Category;
import org.springframework.stereotype.Repository;

/**
 * Repository for Category entities.
 *
 * Provides basic persistence operations for desk game categories.
 */
@Repository
public class CategoryRepository extends BaseRepository<Category> {

    public CategoryRepository() {
        super(Category.class);
    }
}
