package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.Enums.Priority;

class ProjectTest {
	//Tests	
	@Test
	void testAddIssue() {
		Project p = new Project("new project");
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));
		
		assertEquals(4, p.getIssues().size());
		
	}
	
	@Test
	void testClearIssues() {
		Project p = new Project("new project");
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));
		p.addIssue( new Task("Fix  bug", "This description", Priority.LOW));

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
