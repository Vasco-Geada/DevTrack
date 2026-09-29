package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Issue;
import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.Enums.Priority;

class ProjectTest {
	// Tests

	@Test
	void testAddIssue() {
		Project p = new Project("new project");
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));

		assertEquals(4, p.getIssues().size());

	}

	@Test
	void testClearIssues() {
		Project p = new Project("new project");
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue(new Task("Fix  bug", "This description", Priority.LOW));

		assertThrows(UnsupportedOperationException.class, () -> {
			p.getIssues().clear();
		});

		assertEquals(2, p.getIssues().size());
	}

	@Test
	void testProjectString() {
		assertThrows(InvalidEntityException.class, () -> {
			new Project("");
		});

		assertThrows(InvalidEntityException.class, () -> {
			new Project(null);
		});

	}

	@Test
	void testFindExistingIssueById() {
		Project p = new Project("name");
		
		Task task = new Task("Fix login bug", "This is a description", Priority.LOW);
		
		p.addIssue(task);
		
		Optional<Issue> issue = p.findIssueById(task.getId());
		
		assertEquals(true, issue.isPresent());
	}
	
	@Test
	void testFindNonExistingIssueById() {
		Project p = new Project("name");
		
		Task task = new Task("Fix login bug", "This is a description", Priority.LOW);
		
		p.addIssue(task);
		
		Optional<Issue> issue = p.findIssueById("asdasdasd123123ahggee");
		
		assertEquals(false, issue.isPresent());
	}


	@Test
	void testSetName() {
		Project p = new Project("name");
		assertThrows(InvalidEntityException.class, () -> {
			p.setName(null);
		});
		assertThrows(InvalidEntityException.class, () -> {
			p.setName("   ");
		});
	}
}
