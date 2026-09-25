package devtrack.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import devtrack.model.Issue;
import devtrack.model.Project;
import devtrack.model.User;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;

public class IssueService {

	// Methods
	
	//Find By
	public List<Issue> findByStatus(Project project, Status status) {
		if (project == null || status == null) {
			throw new IllegalStateException("Project or user are null");
		}
		
		if ( project.getIssues().size() < 1) {
			return  new ArrayList<>();
		}

		return project.getIssues().stream().filter(issue -> issue.getStatus().equals(status)).toList();
	}

	public List<Issue> findByPriority(Project project, Priority priority) {
		if (project == null || priority == null) {
			throw new IllegalStateException("Project or user are null");
		}
		
		if ( project.getIssues().size() < 1) {
			return  new ArrayList<>();
		}
		
		return project.getIssues().stream().filter(issue -> issue.getPriority().equals(priority)).toList();
	}

	public List<Issue> findAssignTo(Project project, User user) {
		if (project == null || user == null) {
			throw new IllegalStateException("Project/user is null");
		}
		
		if ( project.getIssues().size() < 1) {
			return  new ArrayList<>();
		}
		

		return project.getIssues().stream().filter(issue -> user.equals(issue.getAssignedUser())).toList();
	}
	//Order by
	public List<Issue> orderByPriority(Project project) {
		if (project == null || !(project instanceof Project)) {
			throw new IllegalStateException("Project or user are null");
		}
		
		if ( project.getIssues().size() < 2) {
			return  new ArrayList<>();
		}

		return project.getIssues().stream().sorted(Comparator.comparing(Issue::getPriority).thenComparing(Issue::getPriority)).toList().reversed();
	}

	//Search 
	public List<Issue> searchByTitle(Project project, String title) {
		if (project == null || title == null) {
			throw new IllegalStateException("Project or user are null");
		}
		
		if ( project.getIssues().size() < 1) {
			return  new ArrayList<>();
		}

		return project.getIssues().stream().filter(issue -> issue.getTitle().contains(title.toLowerCase())).toList();
	}
	
	//Count
	public long countOpenIssues(Project project) {
		if (project == null) {
			throw new IllegalStateException("Project or user are null");
		}
		
		if ( project.getIssues().size() < 1) {
			return  0;
		}

		return project.getIssues().stream().filter(issue -> issue.getStatus().equals(Status.OPEN)).count();
	}
}
