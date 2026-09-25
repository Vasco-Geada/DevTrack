package devtrack;

import devtrack.model.Project;
import devtrack.model.Task;
import devtrack.model.User;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Severity;
import devtrack.model.Bug;
import devtrack.model.Feature;

public class Main {

	static Project devtrack = new Project("DevTrack", "A temp project description");

	// Methods
	public static void main(String[] args) {
		System.out.println("Project: " + devtrack.getName());
		System.out.println("");
		System.out.println("Issues");
		User admin = new User("Admin", "admin@email.com");

		devtrack.addIssue(
				new Bug("Implement authentication", "This is  a description", Priority.HIGH, Severity.CRITICAL));
		devtrack.addIssue(new Task("Fix login bug", "This is a description", Priority.LOW));
		devtrack.addIssue(new Feature("Fix login bug", "This is a description", Priority.MEDIUM));
		
		devtrack.getIssue(0).assignTo(admin);
		for (int i = 0; i < devtrack.getIssues().size(); i++) {
			System.out.println("");
			System.out.println("#" + (i + 1) + " " + devtrack.getIssue(i).getDetails());

		}

	}
}
