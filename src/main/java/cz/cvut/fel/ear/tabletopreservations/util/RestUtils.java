package cz.cvut.fel.ear.tabletopreservations.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Utility class for REST-related operations.
 */
public final class RestUtils {

    private RestUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Creates a resource URL from the current request context.
     */
    public static String createResourceUrl(Integer id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUriString();
    }

    /**
     * Creates a resource URL from a specific base path.
     */
    public static String createResourceUrl(String basePath, Integer id) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(basePath)
                .path("/{id}")
                .buildAndExpand(id)
                .toUriString();
    }
}

