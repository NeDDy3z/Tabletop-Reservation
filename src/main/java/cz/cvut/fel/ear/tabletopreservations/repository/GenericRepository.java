package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Base interface for data access objects.
 *
 * @param <T>
 */
public interface GenericRepository<T extends AbstractEntity> {
    /**
     * Finds entity instance with the specified identifier.
     *
     * @param id Identifier
     * @return Entity instance or {@code null} if no such instance exists
     */
    T find(Integer id);

    /**
     * Finds all instances of the specified class.
     *
     * @return List of instances, possibly empty
     */
    List<T> findAll();

    /**
     * Persists the specified entity.
     *
     * @param entity Entity to persist
     */
    void persist(T entity);

    /**
     * Persists the specified instances.
     *
     * @param entities Entities to persist
     */
    default void persist(Collection<T> entities) {
        Objects.requireNonNull(entities);
        entities.forEach(this::persist);
    }

    /**
     * Updates the specified entity.
     *
     * @param entity Entity to update
     * @return The updated instance
     */
    T update(T entity);

    /**
     * Removes the specified entity.
     *
     * @param entity Entity to remove
     */
    void remove(T entity);

    /**
     * Checks whether an entity with the specified id exists (and has the type managed by this DAO).
     *
     * @param id Entity identifier
     * @return {@literal true} if entity exists, {@literal false} otherwise
     */
    default boolean exists(Integer id) {
        return id != null && find(id) != null;
    }
}
