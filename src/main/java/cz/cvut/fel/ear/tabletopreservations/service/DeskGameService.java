package cz.cvut.fel.ear.tabletopreservations.service;

import cz.cvut.fel.ear.tabletopreservations.exception.NotFoundException;
import cz.cvut.fel.ear.tabletopreservations.model.Category;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGame;
import cz.cvut.fel.ear.tabletopreservations.model.DeskGameItem;
import cz.cvut.fel.ear.tabletopreservations.repository.DeskGameItemRepository;
import cz.cvut.fel.ear.tabletopreservations.repository.DeskGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Service for managing desk games and related operations (search, updates and category assignment).
 */
@Service
@Transactional
public class DeskGameService {

    private final DeskGameRepository deskGameRepository;
    private final DeskGameItemRepository deskGameItemRepository;

    @Autowired
    public DeskGameService(DeskGameRepository deskGameRepository, DeskGameItemRepository deskGameItemRepository) {
        this.deskGameRepository = deskGameRepository;
        this.deskGameItemRepository = deskGameItemRepository;
    }

    /**
     * Returns all desk games.
     */
    @Transactional(readOnly = true)
    public List<DeskGame> findAll() {
        return deskGameRepository.findAll();
    }

    /**
     * Finds a desk game by ID.
     */
    @Transactional(readOnly = true)
    public DeskGame find(Integer id) {
        Objects.requireNonNull(id);
        DeskGame deskGame = deskGameRepository.find(id);
        if (deskGame == null) {
            throw NotFoundException.create("DeskGame", id);
        }
        return deskGame;
    }

    /**
     * Finds desk games by title.
     */
    @Transactional(readOnly = true)
    public List<DeskGame> findByTitle(String title) {
        return deskGameRepository.findByTitle(title);
    }

    /**
     * Finds desk games playable by the given number of players or fewer.
     */
    @Transactional(readOnly = true)
    public List<DeskGame> findByMaxPlayers(int maxPlayers) {
        return deskGameRepository.findByMaxPlayers(maxPlayers);
    }

    /**
     * Persists a new desk game.
     */
    public void persist(DeskGame deskGame) {
        Objects.requireNonNull(deskGame);
        deskGameRepository.persist(deskGame);
    }

    /**
     * Updates an existing desk game.
     */
    public void update(DeskGame deskGame) {
        Objects.requireNonNull(deskGame);
        deskGameRepository.update(deskGame);
    }

    /**
     * Removes a desk game and its related desk game items to prevent foreign key constraint violations.
     */
    public void remove(DeskGame deskGame) {
        Objects.requireNonNull(deskGame);
        List<DeskGameItem> items = deskGameItemRepository.findByDeskGame(deskGame);
        for (DeskGameItem item : items) {
            deskGameItemRepository.remove(item);
        }
        deskGameRepository.remove(deskGame);
    }

    /**
     * Applies partial updates from request into the original desk game.
     */
    public void updateDeskGame(DeskGame original, DeskGame request) {
        Objects.requireNonNull(original);
        Objects.requireNonNull(request);

        if (request.getTitle() != null) {
            original.setTitle(request.getTitle());
        }

        if (request.getPublisher() != null) {
            original.setPublisher(request.getPublisher());
        }

        if (request.getMinAge() > 0) {
            original.setMinAge(request.getMinAge());
        }

        if (request.getMinPlayers() > 0) {
            original.setMinPlayers(request.getMinPlayers());
        }

        if (request.getMaxPlayers() > 0) {
            original.setMaxPlayers(request.getMaxPlayers());
        }

        if (request.getAveragePlaytimeMinutes() > 0) {
            original.setAveragePlaytimeMinutes(request.getAveragePlaytimeMinutes());
        }

        deskGameRepository.update(original);
    }

    /**
     * Assigns a category to the given desk game.
     */
    public void addCategory(DeskGame deskGame, Category category) {
        Objects.requireNonNull(deskGame);
        Objects.requireNonNull(category);

        deskGame.getCategories().add(category);

        deskGameRepository.update(deskGame);
    }

    /**
     * Removes a category from the given desk game.
     */
    public void removeCategory(DeskGame deskGame, Category category) {
        Objects.requireNonNull(deskGame);
        Objects.requireNonNull(category);

        deskGame.getCategories().remove(category);

        deskGameRepository.update(deskGame);
    }
}
