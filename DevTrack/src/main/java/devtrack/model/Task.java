package devtrack.model;

import devtrack.exception.InvalidEntityException;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;

public class Task extends Issue {
	// Attributes
	private double estimatedHours = 0;

	// Constructors

	public Task(String title) {
		super(title);
	}

	public Task(String title, String description) {
		super(title, description);
	}

	public Task(String title, String description, Priority priority) {
		super(title, description, priority);
	}

	public Task(String title, String description, Priority priority, double estimatedHours) {
		super(title, description, priority);
		
		if ((estimatedHours <= 0)) {
			throw new InvalidEntityException("estimatedHours não pode ser inferior a 0");
		}

		this.estimatedHours = estimatedHours;
	}

	public Task(String title, String description, Status status, Priority priority) {
		super(title, description, status, priority);
	}

	public Task(String title, String description, Status status, Priority priority, double estimatedHours) {
		super(title, description, status, priority);

		if ((estimatedHours < 0)) {
			throw new InvalidEntityException("estimatedHours não pode ser inferior a 0");
		}
		
		this.estimatedHours = estimatedHours;
	}

	// Getters
	public double getEstimatedHours() {
		return estimatedHours;
	}

	// Setters
	public void setEstimatedHours(double estimatedHours) {
		if ((estimatedHours < 0)) {
			throw new InvalidEntityException("estimatedHours não pode ser inferior a 0");
		}
		
		this.estimatedHours = estimatedHours;
	}

	// Methods
	@Override
	public String getDetails() {

		String returnText = "Task: " + this.getTitle() + "\n" + "Priority: " + this.getPriority() + "\n"
				+ "Estimated Time In Hours: " + this.getEstimatedHours() + "\n" + "Status: " + this.getStatus() + "\n"
				+ "User: ";

		if (this.getAssignedUser() != null) {
			returnText += this.getAssignedUser().getName();
		} else {
			returnText += "Unassigned";
		}

		return returnText;
	}
}
