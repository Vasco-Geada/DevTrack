package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import devtrack.model.Issue;
import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.Enums.Priority;
import devtrack.repository.InMemoryIssueRepository;
import devtrack.service.IssueService;

class IssueServiceTest {
	// Tests
	@Test
	void save_true_test() {
		Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

		IssueService issueService = new IssueService(new InMemoryIssueRepository(new HashMap<String, Issue>()));

		issueService.getRepository().save(task);

		assertEquals(1, issueService.getRepository().findAll().size());
	}

	@Test
	void findById_true_Test() {
		Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

		IssueService issueService = new IssueService(new InMemoryIssueRepository(new HashMap<String, Issue>()));

		issueService.getRepository().save(task);

		assertEquals(Optional.of(task), issueService.getRepository().findById(task.getId()));
	}

	@Test
	void findByIdNonExisting_true_Test() {
		Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

		IssueService issueService = new IssueService(new InMemoryIssueRepository(new HashMap<String, Issue>()));

		issueService.getRepository().save(task);

		assertEquals(Optional.empty(), issueService.getRepository().findById("123123123"));
	}

	@Test
	void deleteById_true_Test() {
		Task task = new Task("Implement LOGIN", "This description", Priority.LOW);

		IssueService issueService = new IssueService(new InMemoryIssueRepository(new HashMap<String, Issue>()));

		issueService.getRepository().save(task);

		issueService.getRepository().deleteById(task.getId());

		assertEquals(0, issueService.getRepository().findAll().size());
	}

	@Test
	void searchTitle_Test() {
		Project p = new Project("new project");
		p.addIssue(new Task("Implement LOGIN", "This description", Priority.LOW));
		p.addIssue(new Task("Implement UI", "This description", Priority.CRITICAL));
		p.addIssue(new Task("Fix login bug", "This description", Priority.MEDIUM));
		p.addIssue(new Task("Create dashboard", "This description", Priority.HIGH));

		IssueService issueService = new IssueService(new InMemoryIssueRepository());
		;

		List<Issue> list = issueService.searchByTitle(p, "login");

		assertEquals(2, list.size());
	}

	@Test
	void orderBy_Test() {
		Project p = new Project("new project");
		p.addIssue(new Task("Implement LOGIN", "This description", Priority.LOW));

		IssueService issueService = new IssueService(new InMemoryIssueRepository());
		;

		/* ORDER BY WITH 1 TASK, CHECK IF RETURNS THE LIST WITH 1 ELEMENT */

		List<Issue> list = issueService.orderByPriority(p);

		assertEquals(1, list.size());

		/*
		 * ORDER BY WITH MULTIPLE TASKS, CHECK IF THE TASK ARE ORDERED BY PRIORITY
		 * (CRITICAL -> LOW)
		 */
		p.addIssue(new Task("Implement UI", "This description", Priority.CRITICAL));
		p.addIssue(new Task("Fix login bug", "This description", Priority.MEDIUM));
		p.addIssue(new Task("Create dashboard", "This description", Priority.HIGH));
		list = issueService.orderByPriority(p);

		assertEquals(Priority.CRITICAL, list.get(0).getPriority());
		assertEquals(Priority.HIGH, list.get(1).getPriority());
		assertEquals(Priority.MEDIUM, list.get(2).getPriority());
		assertEquals(Priority.LOW, list.get(3).getPriority());

	}
}
