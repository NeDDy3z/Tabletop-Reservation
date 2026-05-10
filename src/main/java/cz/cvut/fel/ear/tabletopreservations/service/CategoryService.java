package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.Category;
import cz.cvut.fel.ear.tabletopreservations.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing categories.
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Returns all categories.
     */
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * Finds a category by ID.
     */
    @Transactional(readOnly = true)
    public Category find(Integer id) {
        Objects.requireNonNull(id);
        Category category = categoryRepository.find(id);
        if (category == null) {
            throw NotFoundException.create("Category", id);
        }
        return category;
    }

    /**
     * Persists a new category.
     */
    public void persist(Category category) {
        Objects.requireNonNull(category);
        categoryRepository.persist(category);
    }

    /**
     * Updates an existing category.
     */
    public void update(Category category) {
        Objects.requireNonNull(category);
        categoryRepository.update(category);
    }

    /**
     * Removes a category.
     */
    public void remove(Category category) {
        Objects.requireNonNull(category);
        categoryRepository.remove(category);
    }
}
