package tests;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.exception.InvalidIssueStateException;
import devtrack.model.Enums.ActivityType;
import devtrack.model.Enums.Priority;
import devtrack.model.Issue;
import devtrack.model.IssueActivity;
import devtrack.model.Task;
import devtrack.repository.InMemoryRepository;
import devtrack.service.IssueService;

class IssueServiceTest {
    // Tests

    @Test
    void service_true_test() {
        InMemoryRepository<Issue, String> inMemoryRepository = new InMemoryRepository<>();
        IssueService issueService = new IssueService(inMemoryRepository);
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        // Create IssueService with null repository, should throw InvalidEntityException
        assertThrows(InvalidEntityException.class, () -> {
            IssueService issueService2 = new IssueService(null);
        });

        // Search by status with null status, should throw IllegalStateException
        issueService.createIssue(task);

        List<IssueActivity> oldLog = issueService.findById(task.getId()).get().getActivityLog();
        assertThrows(InvalidIssueStateException.class, () -> {
            issueService.completeIssue(task.getId());
        });

        assertEquals(true, oldLog.equals(issueService.findById(task.getId()).get().getActivityLog()));
        issueService.startIssue(task.getId());
        assertEquals(issueService.findById(task.getId()).get().getStatus(), devtrack.model.Enums.Status.IN_PROGRESS);
        assertEquals(oldLog.size() + 1, issueService.findById(task.getId()).get().getActivityLog().size());

        assertEquals(ActivityType.STATUS_CHANGED, issueService.findById(task.getId()).get().getActivityLog().getFirst().getType());
        assertEquals("OPEN -> IN_PROGRESS", issueService.findById(task.getId()).get().getActivityLog().getFirst().getDescription());

        assertThrows(InvalidIssueStateException.class, () -> {
            issueService.startIssue(task.getId());
        });

        assertEquals(1, inMemoryRepository.findAll().size());
    }

    @Test
    void save_true_test() {
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        InMemoryRepository<Issue, String> inMemoryRepository = new InMemoryRepository<>();
        inMemoryRepository.save(task);

        assertEquals(1, inMemoryRepository.findAll().size());
    }

    @Test
    void findById_true_Test() {
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        InMemoryRepository<Issue, String> inMemoryRepository = new InMemoryRepository<>();
        inMemoryRepository.save(task);

        assertEquals(Optional.of(task), inMemoryRepository.findById(task.getId()));
    }

    @Test
    void findByIdNonExisting_true_Test() {
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        InMemoryRepository<Issue, String> inMemoryRepository = new InMemoryRepository<>();
        inMemoryRepository.save(task);

        assertEquals(Optional.empty(), inMemoryRepository.findById("123123123"));
    }

    @Test
    void deleteById_true_Test() {
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

        InMemoryRepository<Issue, String> inMemoryRepository = new InMemoryRepository<>();

        inMemoryRepository.save(task);

        inMemoryRepository.deleteById(task.getId());

        assertEquals(0, inMemoryRepository.findAll().size());
    }

    @Test
    void searchTitle_Test() {
        IssueService issueService = new IssueService(new InMemoryRepository<>());
        issueService.createIssue(new Task("Implement LOGIN", "This description", Priority.LOW));
        issueService.createIssue(new Task("Implement UI", "This description", Priority.CRITICAL));
        issueService.createIssue(new Task("Fix login bug", "This description", Priority.MEDIUM));
        issueService.createIssue(new Task("Create dashboard", "This description", Priority.HIGH));

        List<Issue> list = issueService.searchByTitle("login");

        assertEquals(2, list.size());
        list = issueService.searchByTitle("mario");

        assertEquals(0, list.size());
    }

    @Test
    void orderBy_Test() {
        IssueService issueService = new IssueService(new InMemoryRepository<>());
        issueService.createIssue(new Task("Implement LOGIN", "This description", Priority.LOW));


        /* ORDER BY WITH 1 TASK, CHECK IF RETURNS THE LIST WITH 1 ELEMENT */
        List<Issue> list = issueService.orderByPriority();

        assertEquals(1, list.size());

        /*
		 * ORDER BY WITH MULTIPLE TASKS, CHECK IF THE TASK ARE ORDERED BY PRIORITY
		 * (CRITICAL -> LOW)
         */
        issueService.createIssue(new Task("Implement UI", "This description", Priority.CRITICAL));
        issueService.createIssue(new Task("Fix login bug", "This description", Priority.MEDIUM));
        issueService.createIssue(new Task("Create dashboard", "This description", Priority.HIGH));
        list = issueService.orderByPriority();

        assertEquals(Priority.CRITICAL, list.get(0).getPriority());
        assertEquals(Priority.HIGH, list.get(1).getPriority());
        assertEquals(Priority.MEDIUM, list.get(2).getPriority());
        assertEquals(Priority.LOW, list.get(3).getPriority());

    }
}
