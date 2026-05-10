package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.Category;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGame;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.service.CategoryService;
import cz.cvut.fel.ear.tabletopreservations.service.DeskGameService;
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
 * REST controller for managing desk games.
 *
 * Allows browsing, searching and administration of board game definitions.
 */
@RestController
@RequestMapping(DeskGameController.BASE_URL)
public class DeskGameController {

    public static final String BASE_URL = "/rest/deskgames";

    private static final Logger LOG = LoggerFactory.getLogger(DeskGameController.class);

    private final DeskGameService deskGameService;
    private final CategoryService categoryService;

    @Autowired
    public DeskGameController(DeskGameService deskGameService, CategoryService categoryService) {
        this.deskGameService = deskGameService;
        this.categoryService = categoryService;
    }

    /**
     * Returns all desk games.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeskGame> getDeskGames() {
        return deskGameService.findAll();
    }

    /**
     * Returns a single desk game by ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeskGame getDeskGame(@PathVariable Integer id) {
        return deskGameService.find(id);
    }

    /**
     * Searches desk games by title.
     */
    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DeskGame> searchByTitle(@RequestParam String title) {
        return deskGameService.findByTitle(title);
    }

    /**
     * Returns all categories assigned to the given desk game.
     */
    @GetMapping(value = "/{id}/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories(@PathVariable Integer id) {
        final DeskGame deskGame = deskGameService.find(id);
        return deskGame.getCategories();
    }

    /**
     * Creates a new desk game.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createDeskGame(@RequestBody DeskGame deskGame) {
        deskGameService.persist(deskGame);

        LOG.debug("Created desk game {}.", deskGame);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(deskGame.getId()));
    }

    /**
     * Updates an existing desk game.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdatedResponse> updateDeskGame(@PathVariable Integer id, @RequestBody DeskGame request) {
        final DeskGame original = deskGameService.find(id);

        deskGameService.updateDeskGame(original, request);

        LOG.debug("Updated desk game {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Deletes a desk game.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDeskGame(@PathVariable Integer id) {
        final DeskGame deskGame = deskGameService.find(id);

        deskGameService.remove(deskGame);

        LOG.debug("Removed desk game {}.", deskGame);
    }

    /**
     * Assigns a category to a desk game.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}/categories/{categoryId}")
    public ResponseEntity<UpdatedResponse> addCategory(@PathVariable Integer id, @PathVariable Integer categoryId) {
        final DeskGame deskGame = deskGameService.find(id);
        final Category category = categoryService.find(categoryId);

        deskGameService.addCategory(deskGame, category);

        LOG.debug("Added category {} to desk game {}.", category, deskGame);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(deskGame.getId(), BASE_URL));
    }

    /**
     * Removes a category from a desk game.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}/categories/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeCategory(@PathVariable Integer id, @PathVariable Integer categoryId) {
        final DeskGame deskGame = deskGameService.find(id);
        final Category category = categoryService.find(categoryId);

        deskGameService.removeCategory(deskGame, category);

        LOG.debug("Removed category {} from desk game {}.", category, deskGame);
    }
}
