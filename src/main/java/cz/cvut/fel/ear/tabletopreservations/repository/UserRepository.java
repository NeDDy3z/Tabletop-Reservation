package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.Role;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Repository for User entities.
 */
@Repository
public class UserRepository extends BaseRepository<User> {

    public UserRepository() {
        super(User.class);
    }

    /**
     * Finds a user by email (case-insensitive).
     */
    public User findByEmail(String email) {
        return em.createNamedQuery("User.findByEmail", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds users by first name (case-insensitive).
     */
    public List<User> findByFirstName(String firstName) {
        List<User> result = em.createNamedQuery("User.findByFirstName", User.class)
                .setParameter("firstName", firstName)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds users by last name (case-insensitive).
     */
    public List<User> findByLastName(String lastName) {
        List<User> result = em.createNamedQuery("User.findByLastName", User.class)
                .setParameter("lastName", lastName)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all active users.
     */
    public List<User> findActiveUsers() {
        List<User> result = em.createNamedQuery("User.findActiveUsers", User.class)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds all inactive users.
     */
    public List<User> findInactiveUsers() {
        List<User> result = em.createNamedQuery("User.findInactiveUsers", User.class)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds users with the given role.
     */
    public List<User> findByRole(Role role) {
        List<User> result = em.createNamedQuery("User.findByRole", User.class)
                .setParameter("role", role)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
