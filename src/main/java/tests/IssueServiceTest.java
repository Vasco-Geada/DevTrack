package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import devtrack.model.Issue;
import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.Enums.Priority;
import devtrack.service.IssueService;

class IssueServiceTest {
	// Attributes

	// Tests
	@Test
	void searchTitle_Test() {
		Project p = new Project("new project");
		p.addIssue( new Task("Implement LOGIN", "This description", Priority.LOW));
		p.addIssue( new Task("Implement UI", "This description", Priority.CRITICAL));
		p.addIssue( new Task("Fix login bug", "This description", Priority.MEDIUM));
		p.addIssue( new Task("Create dashboard", "This description", Priority.HIGH));
		
		IssueService issueService = new IssueService();
		
		
		List<Issue> list = issueService.searchByTitle(p, "login");
		
		assertTrue(list.size() == 2);
		
		
	}
	
	
	@Test
	void orderBy_Test() {
		Project p = new Project("new project");
		p.addIssue( new Task("Implement LOGIN", "This description", Priority.LOW));

		
		IssueService issueService = new IssueService();
		
		/* ORDER BY WITH 1 TASK, CHECK IF RETURNS THE LIST WITH 1 ELEMENT */
		
		List<Issue> list =  issueService.orderByPriority(p);
		System.out.println(list.size());
		assertTrue(list.size() == 1);

		/* ORDER BY WITH MULTIPLE TASKS, CHECK IF THE TASK ARE ORDERED BY PRIORITY (CRITICAL -> LOW)*/
		p.addIssue( new Task("Implement UI", "This description", Priority.CRITICAL));
		p.addIssue( new Task("Fix login bug", "This description", Priority.MEDIUM));
		p.addIssue( new Task("Create dashboard", "This description", Priority.HIGH));
		list =  issueService.orderByPriority(p);
		
		assertTrue(list.get(0).getPriority() == Priority.CRITICAL);
		assertTrue(list.get(1).getPriority() == Priority.HIGH);
		assertTrue(list.get(2).getPriority() == Priority.MEDIUM);
		assertTrue(list.get(3).getPriority() == Priority.LOW);
		
		
	}
}
