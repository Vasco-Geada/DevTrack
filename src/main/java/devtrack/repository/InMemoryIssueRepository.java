package devtrack.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Issue;

public class InMemoryIssueRepository implements Repository<Issue, String> {
    // Attributes

    private final Map<String, Issue> data;

    // Constructors
    public InMemoryIssueRepository() {
        this.data = new HashMap<>();
    }

    public InMemoryIssueRepository(Map<String, Issue> data) {
        if (data == null) {
            throw new InvalidEntityException("Data cannot be null");
        }

        this.data = new HashMap<>(data);
    }

    // Getters
    @Override
    public Optional<Issue> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidEntityException("Id inválido");
        }

        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Issue> findAll() {
        List<Issue> result = new ArrayList<>(data.values());

        return List.copyOf(result);
    }

    // Setters
    @Override
    public void save(Issue entity) {
        if (entity == null) {
            throw new InvalidEntityException("Issue inválida");
        }

        if (this.data.containsKey(entity.getId())) {
            throw new InvalidEntityException("Issue já existe");
        }

        this.data.put(entity.getId(), entity);
    }

    //Methods
    @Override
    public void deleteById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidEntityException("Id inválido");
        }

        if (this.data.get(id) != null) {
            this.data.remove(id);
        } else {

            throw new InvalidEntityException("Issue não existe");
        }

    }
}
