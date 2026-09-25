package devtrack.model;

import java.util.ArrayList;
import java.util.List;

import devtrack.exception.InvalidEntityException;
import devtrack.utils.MainUtils;

public class Project {
	// ########## Attributes
	private final String id;
	private String name;
	private String description;
	private List<Issue> issues = new ArrayList<>();

	// ########## Constructors

	public Project(String name) {
		if (name == null) {
			throw new InvalidEntityException("name não pode ser null");
		} else if (name.isBlank()) {

			throw new InvalidEntityException("name não pode ser vazio");
		}

		this.id = MainUtils.generateId();
		this.name = name;
		this.description = "";
	}

	public Project(String name, String description) {
		this(name);
		setDescription(description);
	}

	public Project(String name, String description, List<Issue> issues) {
		this(name, description);
		setIssues(issues);
	}

	// ########## Getters
	protected String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public List<Issue> getIssues() {
		return List.copyOf(issues);
	}

	public Issue getIssue(int index) {
		return issues.get(index);
	}

	// ########## Setters
	private void setIssues(List<Issue> issues) {
		if ((issues == null)) {
			throw new InvalidEntityException("issues não pode ser null");
		}
		this.issues = new ArrayList<>(issues);
	}

	public void setName(String name) {
		if (name == null) {
			throw new InvalidEntityException("name não pode ser null");
		} else if (name.isBlank()) {

			throw new InvalidEntityException("name não pode ser vazio");
		}

		this.name = name;

	}

	public void setDescription(String description) {

		if (description == null) {
			throw new InvalidEntityException("description não pode ser null");
		}
		this.description = description;
	}

	// ########## Methods
	public void addIssue(Issue issue) {

		if ((issue == null)) {
			throw new InvalidEntityException("issue não pode ser null");
		}

		this.issues.add(issue);

	}

}
