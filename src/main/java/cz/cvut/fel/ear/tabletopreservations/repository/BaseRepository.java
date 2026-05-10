package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.AbstractEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import java.util.Objects;

/**
 * Base repository providing common CRUD operations.
 *
 * Used as a superclass for all entity-specific repositories.
 */
public abstract class BaseRepository<T extends AbstractEntity> implements GenericRepository<T> {

    @PersistenceContext
    protected EntityManager em;

    protected final Class<T> type;

    protected BaseRepository(Class<T> type) {
        this.type = type;
    }

    /**
     * Finds an entity by its primary key.
     */
    @Override
    public T find(Integer id) {
        Objects.requireNonNull(id);
        return em.find(type, id);
    }

    /**
     * Returns all entities of the given type.
     */
    @Override
    public List<T> findAll() {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(type);
            Root<T> root = cq.from(type);
            cq.select(root);
            return em.createQuery(cq).getResultList();
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Persists a new entity.
     */
    @Override
    public void persist(T entity) {
        Objects.requireNonNull(entity);
        try {
            em.persist(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Updates an existing entity.
     */
    @Override
    public T update(T entity) {
        Objects.requireNonNull(entity);
        try {
            return em.merge(entity);
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Removes an entity from persistence.
     */
    @Override
    public void remove(T entity) {
        Objects.requireNonNull(entity);
        try {
            if (em.contains(entity)) {
                em.remove(entity);
                return;
            }
            final T toRemove = em.find(type, entity.getId());
            if (toRemove != null) {
                em.remove(toRemove);
            }
        } catch (RuntimeException e) {
            throw new PersistenceException(e);
        }
    }
}
