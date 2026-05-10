package cz.cvut.fel.ear.tabletopreservations.security;

import cz.cvut.fel.ear.tabletopreservations.model.*;
import cz.cvut.fel.ear.tabletopreservations.security.model.UserDetailsImpl;
import cz.cvut.fel.ear.tabletopreservations.service.ReservationService;
import cz.cvut.fel.ear.tabletopreservations.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Service for security-related checks that can be used in @PreAuthorize annotations.
 */
@Service("securityService")
public class SecurityService {

    private final VenueService venueService;
    private final ReservationService reservationService;

    @Autowired
    public SecurityService(
            VenueService venueService,
            ReservationService reservationService) {
        this.venueService = venueService;
        this.reservationService = reservationService;
    }

    /**
     * Gets the currently authenticated user.
     */
    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) auth.getPrincipal()).getUser();
        }

        return null;
    }

    /**
     * Checks if the current user is an admin.
     */
    public boolean isAdmin() {
        User user = getCurrentUser();
        return user != null && user.getRoles().contains(Role.ADMIN);
    }

    /**
     * Generic method to check if current user is the owner of a resource or an admin.
     * Works with any entity implementing OwnableResource interface.
     *
     * @param resource the resource to check ownership for
     * @return true if current user is owner or admin, false otherwise
     */
    public boolean isOwnerOrAdmin(Ownable resource) {
        User currentUser = getCurrentUser();

        Objects.requireNonNull(resource, "Resource cannot be null");
        Objects.requireNonNull(currentUser, "User cannot be null");

        if (isAdmin()) return true;

        // Check ownership
        User owner = resource.getOwner();

        return owner != null && owner.getId().equals(currentUser.getId());
    }

    /**
     * Method to check if current user is the user with given id or an admin.
     *
     * @param id the user id to check against
     * @return true if current user is owner or admin, false otherwise
     */
    public boolean isSelfOrAdmin(Integer id) {
        User currentUser = getCurrentUser();

        Objects.requireNonNull(id, "Resource cannot be null");
        Objects.requireNonNull(currentUser, "User cannot be null");

        if (isAdmin()) return true;

        // Check ownership
        return currentUser.getId().equals(id);
    }

    public boolean isReservationProvider(Reservation reservation) {
        User currentUser = getCurrentUser();

        Objects.requireNonNull(reservation, "Reservation cannot be null");
        Objects.requireNonNull(currentUser, "User cannot be null");

        if (isAdmin()) return true;

        // Is reservation PROVIDER related
        return reservation.getTimeSlots().getFirst().getGameTable().getOwner().getId().equals(currentUser.getId());
    }
}
