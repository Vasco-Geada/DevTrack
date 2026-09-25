package devtrack.model;

public interface Assignable {
	void assignTo(User user);

	void removeAssignedUser();

	User getAssignedUser();
}
