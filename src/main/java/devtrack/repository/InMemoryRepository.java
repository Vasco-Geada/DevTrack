package devtrack.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Identifiable;

public class InMemoryRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID> {

    private final Map<ID, T> data;

    // Constructors
    public InMemoryRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public void save(T entity) {
        if (entity == null || entity.getId() == null) {
            throw new InvalidEntityException("Entity/Id inválida");
        }

        if (this.data.containsKey(entity.getId())) {
            throw new InvalidEntityException("Entity já existe");
        }

        this.data.put(entity.getId(), entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        if (id == null) {
            throw new InvalidEntityException("Id inválido");
        }

        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<T> findAll() {
        List<T> result = new ArrayList<>(data.values());

        return List.copyOf(result);
    }

    @Override
    public void deleteById(ID id) {
        if (id == null) {
            throw new InvalidEntityException("Id inválido");
        }

        if (this.data.get(id) != null) {
            this.data.remove(id);
        } else {

            throw new InvalidEntityException("Entity não existe");
        }

    }
}
