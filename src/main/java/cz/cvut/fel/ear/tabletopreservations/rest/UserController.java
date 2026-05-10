package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.exception.ValidationException;
import cz.cvut.fel.ear.tabletopreservations.model.Reservation;
import cz.cvut.fel.ear.tabletopreservations.model.Role;
import cz.cvut.fel.ear.tabletopreservations.model.User;
import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.requests.UserRegistrationRequest;
import cz.cvut.fel.ear.tabletopreservations.service.ReservationService;
import cz.cvut.fel.ear.tabletopreservations.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management.
 */
@RestController
@RequestMapping(UserController.BASE_PATH)
public class UserController {

    public static final String BASE_PATH = "/rest/users";

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final ReservationService reservationService;

    @Autowired
    public UserController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    /**
     * Returns all users (admin only).
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getUsers() {
        return userService.findAll();
    }

    /**
     * Returns user details by ID.
     */
    @PreAuthorize("@securityService.isSelfOrAdmin(#id)")
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUser(@PathVariable Integer id) {
        return userService.find(id);
    }

    /**
     * Returns reservations of the given user.
     */
    @PreAuthorize("@securityService.isSelfOrAdmin(#id)")
    @GetMapping(value = "/{id}/reservations", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Reservation> getUserReservations(@PathVariable Integer id) {
        final User user = userService.find(id);
        return reservationService.findByUser(user);
    }

    /**
     * Returns venues owned by the given user.
     */
    @PreAuthorize("@securityService.isSelfOrAdmin(#id)")
    @GetMapping(value = "/{id}/venues", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Venue> getUserVenues(@PathVariable Integer id) {
        final User user = userService.find(id);
        return user.getVenues();
    }

    /**
     * Creates a new user (admin only).
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createUser(@RequestBody User user) {
        userService.persist(user);

        LOG.debug("Created user {}.", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(user.getId()));
    }

    /**
     * Registers a new user.
     */
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        if (request.getRole() == Role.ADMIN) {
            LOG.debug("Attempt to register user with ADMIN role denied.");
            throw new ValidationException("Registration with ADMIN role is not allowed. Users cannot self-register as administrators.");
        }

        if (userService.findByEmail(request.getEmail()) != null) {
            LOG.debug("Attempt to register user with existing email {} denied.", request.getEmail());
            throw new ValidationException("A user with the provided email already exists.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.addRole(request.getRole());
        user.setActive(true);

        userService.addNewUser(user);

        LOG.debug("Registered new user {}.", user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(user.getId(), BASE_PATH));
    }

    /**
     * Updates user details.
     */
    @PreAuthorize("@securityService.isSelfOrAdmin(#id)")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UpdatedResponse> updateUser(@PathVariable Integer id, @RequestBody User user) {
        final User original = userService.find(id);

        userService.updateUser(original, user);

        LOG.debug("Updated user {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(user.getId()));
    }

    /**
     * Deletes a user (admin only).
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Integer id) {
        final User user = userService.find(id);

        userService.remove(user);

        LOG.debug("Removed user {}.", user);
    }

    /**
     * Adds a role to a user.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UpdatedResponse> addRole(@PathVariable Integer id, @RequestParam Role role) {
        final User user = userService.find(id);

        userService.addNewRoleToUser(user, role);

        LOG.debug("Added role {} to user {}.", role, user);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(user.getId(), BASE_PATH));
    }

    /**
     * Removes a role from a user.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UpdatedResponse> deleteRole(@PathVariable Integer id, @RequestParam Role role) {
        final User user = userService.find(id);

        userService.removeRoleFromUser(user, role);

        LOG.debug("Remove role {} from user {}.", role, user);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(user.getId(), "/rest/users"));}
}
