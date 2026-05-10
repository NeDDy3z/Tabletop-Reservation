package cz.cvut.fel.ear.tabletopreservations.repository;

import cz.cvut.fel.ear.tabletopreservations.model.Venue;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Repository for Venue entities.
 */
@Repository
public class VenueRepository extends BaseRepository<Venue> {

    public VenueRepository() {
        super(Venue.class);
    }

    /**
     * Finds venues in the given city.
     */
    public List<Venue> findByCity(String city) {
        List<Venue> result = em.createNamedQuery("Venue.findByCity", Venue.class)
                .setParameter("city", city)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds venues on the given street (case-insensitive).
     */
    public List<Venue> findByStreet(String street) {
        List<Venue> result = em.createNamedQuery("Venue.findByStreet", Venue.class)
                .setParameter("street", street)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds venues by postal code.
     */
    public List<Venue> findByPostalCode(String postalCode) {
        List<Venue> result = em.createNamedQuery("Venue.findByPostalCode", Venue.class)
                .setParameter("postalCode", postalCode)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Finds venues in the given country (case-insensitive).
     */
    public List<Venue> findByCountry(String country) {
        List<Venue> result = em.createNamedQuery("Venue.findByCountry", Venue.class)
                .setParameter("country", country)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }

    /**
     * Searches venues by keyword across name, city, street and country.
     */
    public List<Venue> searchByKeyword(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return Collections.emptyList();
        }

        List<Venue> result = em.createNamedQuery("Venue.searchByKeyword", Venue.class)
                .setParameter("keyword", keyword)
                .getResultList();
        return result.isEmpty() ? Collections.emptyList() : result;
    }
}
