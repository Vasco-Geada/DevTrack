package tests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Enums.Priority;
import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.User;
import devtrack.repository.InMemoryRepository;

class InMemoryRepositoryTest {
    // Tests

    @Test
    void saveFindDeleteIssue_true_Test() {
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        InMemoryRepository<Task, String> inMemoryRepository = new InMemoryRepository<>();
        InMemoryRepository<Task, String> repositoryB = new InMemoryRepository<>();

        inMemoryRepository.save(task);

        assertEquals(0, repositoryB.findAll().size());
        assertEquals(1, inMemoryRepository.findAll().size());
        assertEquals(Optional.empty(), repositoryB.findById(task.getId()));

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.save(null);
        });
        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.save(task);
        });

        assertEquals(1, inMemoryRepository.findAll().size());

        List<Task> list = inMemoryRepository.findAll();
        assertThrows(UnsupportedOperationException.class, () -> {
            list.add(new Task("Another Task", "This is another task", Priority.LOW));
        });

        assertEquals(1, inMemoryRepository.findAll().size());

        assertEquals(Optional.of(task), inMemoryRepository.findById(task.getId()));

        assertEquals(Optional.empty(), inMemoryRepository.findById("123123123"));
        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.findById(null);
        });

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById("123123123");
        });
        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById(null);
        });

        inMemoryRepository.deleteById(task.getId());
        assertEquals(0, inMemoryRepository.findAll().size());
    }

    @Test
    void saveFindDeleteUser_true_Test() {
        User user = new User("John Doe", "john.doe@example.com");

        InMemoryRepository<User, String> inMemoryRepository = new InMemoryRepository<>();

        inMemoryRepository.save(user);

        assertEquals(1, inMemoryRepository.findAll().size());

        List<User> list = inMemoryRepository.findAll();
        assertThrows(UnsupportedOperationException.class, () -> {
            list.add(new User("Jane Doe", "jane.doe@example.com"));
        });
        assertEquals(1, inMemoryRepository.findAll().size());

        assertEquals(Optional.of(user), inMemoryRepository.findById(user.getId()));

        assertEquals(Optional.empty(), inMemoryRepository.findById("123123123"));
        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.findById(null);
        });

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById("123123123");
        });
        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById(null);
        });

        inMemoryRepository.deleteById(user.getId());
        assertEquals(0, inMemoryRepository.findAll().size());
    }

    @Test
    void saveFindDeleteProject_true_Test() {
        Project project = new Project("My Project", "This is a project");

        InMemoryRepository<Project, String> inMemoryRepository = new InMemoryRepository<>();

        inMemoryRepository.save(project);

        assertEquals(1, inMemoryRepository.findAll().size());

        List<Project> list = inMemoryRepository.findAll();

        assertThrows(UnsupportedOperationException.class, () -> {
            list.add(new Project("Another Project", "This is another project"));
        });

        assertEquals(1, inMemoryRepository.findAll().size());

        assertEquals(Optional.of(project), inMemoryRepository.findById(project.getId()));

        assertEquals(Optional.empty(), inMemoryRepository.findById("123123123"));

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.findById(null);
        });

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById("123123123");
        });

        assertThrows(InvalidEntityException.class, () -> {
            inMemoryRepository.deleteById(null);
        });

        inMemoryRepository.deleteById(project.getId());

        assertEquals(0, inMemoryRepository.findAll().size());
    }
}
