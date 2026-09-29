package devtrack.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import devtrack.exception.InvalidEntityException;
import devtrack.exception.InvalidIssueStateException;
import devtrack.model.Enums.ActivityType;
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
	private final LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private List<IssueActivity> activityLog;

	// Constructors

	public Issue(String title) {
		if ((title == null) || title.isBlank()) {
			throw new InvalidEntityException("title não pode ser null");
		}
		this.activityLog = new ArrayList<IssueActivity>();
		this.id = MainUtils.generateId();
		this.title = title;
		this.description = "";
		this.status = Status.OPEN;
		this.priority = Priority.MEDIUM;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
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

	public String getId() {
		return id;
	}

	public List<IssueActivity> getActivityLog() {
		return List.copyOf(activityLog);
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public User getAssignedUser() {
		return user;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	// Setters

	protected void setUpdatedAt(LocalDateTime time) {
		this.updatedAt = time;

	}

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
		LocalDateTime time = LocalDateTime.now();
		setUpdatedAt(time);
		activityLog.add(new IssueActivity(ActivityType.STATUS_CHANGED, "IN_PROGRESS -> OPEN", time));
	}

	public void start() {
		if (status == null || !status.equals(Status.OPEN)) {
			throw new InvalidIssueStateException("Only an open issue can start");
		}

		setStatus(Status.IN_PROGRESS);
		LocalDateTime time = LocalDateTime.now();
		setUpdatedAt(time);
		activityLog.add(new IssueActivity(ActivityType.STATUS_CHANGED, "OPEN -> IN_PROGRESS", time));

	}

	public void complete() {
		if (status == null || !status.equals(Status.IN_PROGRESS)) {
			throw new InvalidIssueStateException("Only an in progress issue can be completed");
		}

		setStatus(Status.DONE);
		LocalDateTime time = LocalDateTime.now();
		setUpdatedAt(time);
		activityLog.add(new IssueActivity(ActivityType.STATUS_CHANGED, "IN_PROGRESS -> DONE", time));

	}

	public void reOpen() {
		if (status == null || !status.equals(Status.DONE)) {
			throw new InvalidIssueStateException("Only a Done issue can be reopen");
		}

		setStatus(Status.IN_PROGRESS);
		LocalDateTime time = LocalDateTime.now();
		setUpdatedAt(time);
		activityLog.add(new IssueActivity(ActivityType.STATUS_CHANGED, "DONE -> IN_PROGRESS", time));

	}

	public abstract String getDetails();

	@Override
	public void assignTo(User user) {
		if (user == null) {
			throw new InvalidEntityException("Undefined user, please assign a new one");
		}

		if (this.user != null) {
			activityLog.add(new IssueActivity(ActivityType.USER_CHANGED,
					this.getAssignedUser().getName() + " -> " + user.getName(), LocalDateTime.now()));
			
		} else {
			activityLog.add(new IssueActivity(ActivityType.USER_ASSIGNED, "Unassigned -> " + user.getName(),
					LocalDateTime.now()));
		}

		this.setUser(user);
		setUpdatedAt(LocalDateTime.now());
	}

	@Override
	public void removeAssignedUser() {
		if(this.user == null) {
			throw new InvalidEntityException("User is already unassigned");
		}
		activityLog.add(
				new IssueActivity(ActivityType.USER_REMOVED, user.getName() + " -> Unassigned", LocalDateTime.now()));
		this.setUser(null);
		setUpdatedAt(LocalDateTime.now());
	}

}
