package tests;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import devtrack.dto.IssueResponse;
import devtrack.exception.InvalidEntityException;
import devtrack.mapper.IssueMapper;
import devtrack.model.Bug;
import devtrack.model.Enums.IssueType;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Severity;
import devtrack.model.Enums.Status;
import devtrack.model.Feature;
import devtrack.model.Issue;
import devtrack.model.IssueActivity;
import devtrack.model.Task;
import devtrack.model.User;

public class IssueMapperTest {

    @Test
    void testTaskToResponse() {
        IssueMapper issueMapper = new IssueMapper();

        User user1 = new User("Vasco", "email@email.com");
        Task task = new Task("Implement LOGIN", "This description", Priority.LOW, 1, user1);
        IssueResponse response = issueMapper.toResponse(task);

        assertEquals("Implement LOGIN", response.title());
        assertEquals("This description", response.description());
        assertEquals(Priority.LOW, response.priority());
        assertEquals(task.getCreatedAt(), response.createdAt());
        assertEquals(task.getUpdatedAt(), response.updatedAt());
        assertEquals(task.getId(), response.id());
        assertEquals(IssueType.TASK, response.type());
        assertEquals(user1.getId(), response.assignedUserId());
        assertEquals(user1.getName(), response.assignedUserName());
    }

    @Test
    void testBugToResponse() {
        IssueMapper issueMapper = new IssueMapper();

        User user1 = new User("Vasco", "email@email.com");
        Bug bug = new Bug("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, Severity.CRITICAL, user1);

        IssueResponse response = issueMapper.toResponse(bug);

        assertEquals("Implement LOGIN", response.title());
        assertEquals("This description", response.description());
        assertEquals(Status.OPEN, response.status());
        assertEquals(bug.getCreatedAt(), response.createdAt());
        assertEquals(bug.getUpdatedAt(), response.updatedAt());
        assertEquals(Priority.LOW, response.priority());
        assertEquals(bug.getId(), response.id());
        assertEquals(IssueType.BUG, response.type());
    }

    @Test
    void testFeatureToResponse() {
        IssueMapper issueMapper = new IssueMapper();
        Feature feature = new Feature("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, "Criar um botão de login funcional");

        IssueResponse response = issueMapper.toResponse(feature);

        assertEquals("Implement LOGIN", response.title());
        assertEquals("This description", response.description());
        assertEquals(Priority.LOW, response.priority());
        assertEquals(Status.OPEN, response.status());
        assertEquals(feature.getCreatedAt(), response.createdAt());
        assertEquals(feature.getUpdatedAt(), response.updatedAt());
        assertEquals(feature.getId(), response.id());
        assertEquals(IssueType.FEATURE, response.type());

        assertEquals(null, response.assignedUserId());
        assertEquals(null, response.assignedUserName());
    }

    @Test
    void testEmptyMapperList() {
        IssueMapper issueMapper = new IssueMapper();

        List<IssueResponse> responses = issueMapper.toResponses(List.of());

        assertEquals(0, responses.size());
        assertThrows(InvalidEntityException.class, () -> {
            issueMapper.toResponses(null);
        });

    }

    @Test
    void testMapperList() {
        IssueMapper issueMapper = new IssueMapper();

        Task task = new Task("Implement LOGIN", "This description", Priority.LOW, 1);
        Feature feature = new Feature("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, "Criar um botão de login funcional");
        Bug bug = new Bug("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, Severity.CRITICAL);
        List<IssueResponse> responses = issueMapper.toResponses(List.of(task, feature, bug));

        //Test TASK type and id
        assertEquals(task.getId(), responses.get(0).id());
        assertEquals(IssueType.TASK, responses.get(0).type());

        //Test FEATURE type and id
        assertEquals(feature.getId(), responses.get(1).id());
        assertEquals(IssueType.FEATURE, responses.get(1).type());

        //Test BUG type and id
        assertEquals(bug.getId(), responses.get(2).id());
        assertEquals(IssueType.BUG, responses.get(2).type());

        //Check the size of the list
        assertEquals(3, responses.size());

    }

    @Test
    void testMapperListNulls() {
        IssueMapper issueMapper = new IssueMapper();

        Task task = new Task("Implement LOGIN", "This description", Priority.LOW, 1);
        List<Issue> list = new ArrayList<>();
        list.add(task);
        list.add(null);

        assertThrows(InvalidEntityException.class, () -> {
            issueMapper.toResponse(null);
        });
        assertThrows(InvalidEntityException.class, () -> {
            issueMapper.toResponses(null);
        });
        assertThrows(InvalidEntityException.class, () -> {
            issueMapper.toResponses(list);
        });
    }

    @Test
    void testIssueMapperActivityLog() {
        IssueMapper issueMapper = new IssueMapper();

        Task task = new Task("Implement LOGIN", "This description", Priority.LOW, 1);
        List<IssueActivity> activityLog = task.getActivityLog();

        issueMapper.toResponse(task);
        assertEquals(activityLog, task.getActivityLog());
    }

    @Test
    void testIssueStatus() {
        IssueMapper issueMapper = new IssueMapper();

        Task task = new Task("Implement LOGIN", "This description", Status.OPEN, Priority.LOW, 1);

        IssueResponse response = issueMapper.toResponse(task);
        task.start();
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(Status.OPEN, response.status());
    }
}
