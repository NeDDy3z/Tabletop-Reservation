package cz.cvut.fel.ear.tabletopreservations.rest.requests;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for venue creation/update requests.
 */
public class VenueRequest {
    @NotNull(message = "Name must not be null")
    private String name;

    @NotNull(message = "Street must not be null")
    private String street;

    @NotNull(message = "City must not be null")
    private String city;

    @NotNull(message = "Postal code must not be null")
    private String postalCode;

    @NotNull(message = "Country must not be null")
    private String country;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
