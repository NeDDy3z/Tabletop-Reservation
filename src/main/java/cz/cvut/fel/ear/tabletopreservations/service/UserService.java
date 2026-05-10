package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.repository.*;
import cz.cvut.fel.ear.tabletopreservations.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing users, including registration, updates and role handling.
 */
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.userRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Returns all users.
     */
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by ID.
     */
    @Transactional(readOnly = true)
    public User find(Integer id) {
        Objects.requireNonNull(id);
        User user = userRepository.find(id);
        if (user == null) {
            throw NotFoundException.create("User", id);
        }
        return user;
    }

    /**
     * Finds a user by email.
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        Objects.requireNonNull(email);
        return userRepository.findByEmail(email);
    }

    /**
     * Persists a new user.
     */
    public void persist(User user) {
        Objects.requireNonNull(user);
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.persist(user);
    }

    /**
     * Updates an existing user.
     */
    public void update(User user) {
        Objects.requireNonNull(user);
        userRepository.update(user);
    }

    /**
     * Removes a user.
     */
    public void remove(User user) {
        Objects.requireNonNull(user);
        userRepository.remove(user);
    }

    /**
     * Registers a new user and assigns a default role if none is present.
     */
    public void addNewUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.addRole(Role.PLAYER);
        }

        userRepository.persist(user);
    }

    /**
     * Updates user profile information.
     */
    public void updateUser(User original, User request) {
        Objects.requireNonNull(request);

        if (request.getFirstName() != null) {
            original.setFirstName(request.getFirstName().trim());
        }

        if (request.getLastName() != null) {
            original.setLastName(request.getLastName().trim());
        }

        if (request.getEmail() != null) {
            original.setEmail(request.getEmail().trim());
        }

        if (request.getPassword() != null) {
            original.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.update(original);
    }

    /**
     * Adds a role to a user.
     */
    public void addNewRoleToUser(User user, Role role) {
        user.addRole(role);
        userRepository.update(user);
    }

    /**
     * Removes a role from a user and ensures at least one role remains.
     */
    public void removeRoleFromUser(User user, Role role) {
        user.removeRole(role);

        if (user.getRoles().isEmpty()) {
            user.addRole(Role.PLAYER);
        }

        userRepository.update(user);
    }
}
