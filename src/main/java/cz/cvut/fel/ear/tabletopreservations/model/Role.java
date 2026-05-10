package cz.cvut.fel.ear.tabletopreservations.model;

/**
 * Represents a user role used for authorization.
 */
public enum Role {
    ADMIN("ROLE_ADMIN"),
    PLAYER("ROLE_PLAYER"),
    PROVIDER("ROLE_PROVIDER"),
    GUEST("ROLE_GUEST");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
