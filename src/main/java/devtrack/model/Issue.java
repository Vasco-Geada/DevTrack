package devtrack.model;

import devtrack.exception.InvalidEntityException;
import devtrack.exception.InvalidIssueStateException;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;
import devtrack.utils.MainUtils;

public abstract class Issue implements Assignable {
	// Attributes
	private final String id;
	private String title;
	private String description;
	private Status status;
	private Priority priority;
	private User user;

	// Constructors

	public Issue(String title) {
		if ((title == null) || title.isBlank()) {
			throw new InvalidEntityException("title não pode ser null");
		}

		this.id = MainUtils.generateId();
		this.title = title;
		this.description = "";
		this.status = Status.OPEN;
		this.priority = Priority.MEDIUM;
	}

	public Issue(String title, String description) {
		this(title);
		this.description = description;
		this.status = Status.OPEN;
		this.priority = Priority.MEDIUM;
	}

	public Issue(String title, String description, Priority priority) {
		this(title, description);
		if ((priority == null)) {
			throw new InvalidEntityException("priority não pode ser null");
		}
		
		this.status = Status.OPEN;
		this.priority = priority;
	}

	public Issue(String title, String description, Status status, Priority priority) {
		this(title, description, priority);
		

		if ((status == null)) {
			throw new InvalidEntityException("status não pode ser null");
		}
		this.status = status;
	}

	// Getters
	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Status getStatus() {
		return status;
	}

	public Priority getPriority() {
		return priority;
	}

	protected String getId() {
		return id;
	}

	@Override
	public User getAssignedUser() {
		return user;
	}

	// Setters
	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	private void setStatus(Status status) {
		if (status != null) {
			this.status = status;
		}
	}

	protected void setPriority(Priority priority) {
		if (priority != null) {
			this.priority = priority;
		}
	}

	private void setUser(User user) {
		this.user = user;
	}

	/// Methods
	public void restart() {
		if (status == null || !status.equals(Status.IN_PROGRESS)) {
			throw new InvalidIssueStateException("Only an in progress issue can restart");
		}
		
		setStatus(Status.OPEN);
	}

	public void start() {
		if (status == null || !status.equals(Status.OPEN)) {
			throw new InvalidIssueStateException("Only an open issue can start");
		}
		
		setStatus(Status.IN_PROGRESS);
	}

	public void complete() {
		if (status == null || !status.equals(Status.IN_PROGRESS)) {
			throw new InvalidIssueStateException("Only an in progress issue can be completed");
		}
		
		setStatus(Status.DONE);
	}

	public void reOpen() {
		if (status == null || !status.equals(Status.DONE)) {
			throw new InvalidIssueStateException("Only a Done issue can be reopen");
		}
		
		setStatus(Status.IN_PROGRESS);

	}

	public abstract String getDetails();

	@Override
	public void assignTo(User user) {
		if (user == null) {
			throw new InvalidEntityException("Undefined user, please assign a new one");
		}

		this.setUser(user);
	}

	@Override
	public void removeAssignedUser() {
		this.setUser(null);
	}

}
