package cz.cvut.fel.ear.tabletopreservations.rest;

import cz.cvut.fel.ear.tabletopreservations.model.Category;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.CreatedResponse;
import cz.cvut.fel.ear.tabletopreservations.rest.reponses.UpdatedResponse;
import cz.cvut.fel.ear.tabletopreservations.service.CategoryService;
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
 * REST controller for managing game categories.
 *
 * Provides basic CRUD operations over categories.
 */
@RestController
@RequestMapping("/rest/categories")
public class CategoryController {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Returns all categories.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return categoryService.findAll();
    }

    /**
     * Returns a single category by its ID.
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getCategory(@PathVariable Integer id) {
        return categoryService.find(id);
    }

    /**
     * Creates a new category.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreatedResponse> createCategory(@RequestBody Category category) {
        categoryService.persist(category);

        LOG.debug("Created category {}.", category);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CreatedResponse(category.getId()));
    }

    /**
     * Updates an existing category.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<UpdatedResponse> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        final Category original = categoryService.find(id);

        original.setName(category.getName());
        categoryService.update(original);

        LOG.debug("Updated category {}.", original);

        return ResponseEntity.status(HttpStatus.OK).body(new UpdatedResponse(original.getId()));
    }

    /**
     * Deletes a category.
     * Accessible only to administrators.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Integer id) {
        final Category category = categoryService.find(id);

        categoryService.remove(category);

        LOG.debug("Removed category {}.", category);
    }
}
