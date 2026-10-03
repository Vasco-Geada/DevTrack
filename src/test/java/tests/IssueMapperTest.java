package tests;

import org.junit.jupiter.api.Test;

import devtrack.model.Bug;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Severity;
import devtrack.model.Enums.Status;
import devtrack.model.Task;
import devtrack.model.User;
import devtrack.repository.InMemoryRepository;
import devtrack.service.IssueService;

public class IssueMapperTest {

    @Test
    void testRemoveAssignable() {
        IssueService issueService = new IssueService(new InMemoryRepository<>());
        User user1 = new User("Vasco", "email@email.com");
        User user2 = new User("Maria", "email@email.com");

        issueService.createIssue(new Task("Implement LOGIN", "This description", Priority.LOW));
        issueService.createIssue(new Bug("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, Severity.CRITICAL, user1));

        //assertEquals(user2, bug.getAssignedUser());
    }
}
