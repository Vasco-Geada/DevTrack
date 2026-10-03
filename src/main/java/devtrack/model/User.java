package devtrack.model;

import java.util.Objects;

import devtrack.exception.InvalidEntityException;
import devtrack.utils.MainUtils;

public class User implements Identifiable<String> {
    // Attributes

    private final String id;
    private String name;
    private String email;

    // Constructors
    public User(String name, String email) {
        if ((name == null || name.isBlank())) {
            throw new InvalidEntityException("name não pode ser null/vazio");
        }

        validateEmail(email);

        this.id = MainUtils.generateId();

        this.name = name;
        this.email = email;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getId() {
        return id;
    }

    // Setters
    public void setName(String name) {
        if ((name == null || name.isBlank())) {
            throw new InvalidEntityException("name não pode ser null/vazio");
        }
        this.name = name;
    }

    public void setEmail(String email) {
        validateEmail(email);

        this.email = email;
    }

    // Methods
    private void validateEmail(String email) throws InvalidEntityException {

        if ((email == null || email.isBlank())) {
            throw new InvalidEntityException("email não pode ser null/vazio");
        }
        if (!(email.contains("@")) || !(email.contains("."))) {
            throw new InvalidEntityException("email inválido");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User other = (User) obj;
        return Objects.equals(id, other.id);
    }
}
