package devtrack.model;

import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;

public class Feature extends Issue {
	// Attributes
	private String criteria = "Unknown";
	// Constructors

	public Feature(String title) {
		super(title);
	}

	public Feature(String title, String description) {
		super(title, description);
	}

	public Feature(String title, String description, Priority priority) {
		super(title, description, priority);
	}

	public Feature(String title, String description, Priority priority, String criteria) {
		super(title, description, priority);
		this.criteria = criteria;
	}

	public Feature(String title, String description, Status status, Priority priority) {
		super(title, description, status, priority);
	}

	public Feature(String title, String description, Status status, Priority priority, String criteria) {
		super(title, description, status, priority);
		this.criteria = criteria;
	}

	// Getters
	public String getCriteria() {
		return criteria;
	}

	// Setters
	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}

	// Methods
	@Override
	public String getDetails() {
		String returnText = "Feature: " + this.getTitle() + "\n" + "Priority: " + this.getPriority() + "\n"
				+ "Criteria: " + this.getCriteria() + "\n" + "Status: " + this.getStatus() + "\n" + "User: ";
		
		if (this.getAssignedUser() != null) {
			returnText += this.getAssignedUser().getName();
		} else {
			returnText += "Unassigned";
		}
		
		return returnText;
	}
}
