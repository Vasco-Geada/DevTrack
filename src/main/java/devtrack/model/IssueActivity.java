package devtrack.model;

import java.time.LocalDateTime;

import devtrack.model.Enums.ActivityType;

public class IssueActivity {
	// Attributes
	private final ActivityType type;
	private final String description;
	private final LocalDateTime createdAt;

	// Constructors

	public IssueActivity(ActivityType type, String description, LocalDateTime createdAt) {
		this.type = type;
		this.description = description;
		this.createdAt = createdAt;
	}

	// Getters

	public ActivityType getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	// Setters
}
