package devtrack.model;

import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Severity;
import devtrack.model.Enums.Status;

public class Bug extends Issue {
	// Attributes
	private Severity severity;

	// Constructors

	public Bug(String title) {
		super(title);
	}

	public Bug(String title, String description) {
		super(title, description);
	}

	public Bug(String title, String description, Status status, Priority priority) {
		super(title, description, status, priority);
	}

	public Bug(String title, String description, Priority priority, Severity severity) {
		super(title, description, priority);
		this.severity = severity;
	}

	public Bug(String title, String description, Status status, Priority priority, Severity severity) {
		this(title, description, status, priority);
		this.severity = severity;
	}

	// Getters

	public Severity getSeverity() {
		return severity;
	}

	// Setters

	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	// Methods
	@Override
	public String getDetails() {
		String returnText = "Bug: " + this.getTitle() + "\n" + "Priority: " + this.getPriority() + "\n" + "Severitys: "
				+ this.getSeverity() + "\n" + "Status: " + this.getStatus() + "\n" + "User: ";

		if (this.getAssignedUser() != null) {
			returnText += this.getAssignedUser().getName();
		} else {
			returnText += "Unassigned";
		}
		
		return returnText;
	}
}
