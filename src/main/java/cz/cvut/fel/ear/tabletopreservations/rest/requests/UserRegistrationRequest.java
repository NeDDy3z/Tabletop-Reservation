package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import cz.cvut.fel.ear.tabletopreservations.model.Role;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for user registration requests.
 */
public class UserRegistrationRequest {
    @NotNull(message = "First name must not be null")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    private String lastName;

    @NotNull(message = "Email must not be null")
    private String email;

    @NotNull(message = "Password must not be null")
    private String password;

    @NotNull(message = "Role must not be null")
    private String role;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        try {
            return Role.valueOf(this.role.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return Role.PLAYER;
        }
    }

    public void setRole(String role) {
        this.role = role;
    }
}
