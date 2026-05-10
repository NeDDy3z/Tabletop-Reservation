package cz.cvut.fel.ear.tabletopreservations.model;

/**
 * Interface for resources that have an owner.
 * Implement this interface to enable ownership-based security checks.
 */
public interface Ownable {

    /**
     * Returns the owner of this resource.
     */
    User getOwner();
}

