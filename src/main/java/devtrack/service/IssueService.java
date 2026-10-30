package devtrack.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import devtrack.exception.InvalidEntityException;
import devtrack.exception.IssueNotFoundException;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;
import devtrack.model.Issue;
import devtrack.model.User;
import devtrack.repository.Repository;

public class IssueService {

    // Attributes
    private final Repository<Issue, String> repository;

    public IssueService(Repository<Issue, String> repository) {
        if (repository == null) {
            throw new InvalidEntityException("Repository cannot be null");
        }

        this.repository = repository;

    }

    //Create & Delete
    public void createIssue(Issue issue) {
        if (issue == null) {
            throw new InvalidEntityException("Issue is null");
        }

        this.repository.save(issue);
    }

    public void deleteIssue(String id) {
        if (this.repository.findById(id).isEmpty()) {
            throw new IssueNotFoundException("Issue does not exist");
        }

        this.repository.deleteById(id);
    }

    // Filters
    public List<Issue> findByStatusPriorityTitle(Status status, Priority priority, String title) {

        List<Issue> issues = this.findAll();

        if (title != null && !title.isBlank()) {
            issues = this.searchByTitle(title);
        }

        if (status != null) {
            issues = issues.stream()
                    .filter(issue -> issue.getStatus().equals(status))
                    .toList();
        }

        if (priority != null) {
            issues = issues.stream()
                    .filter(issue -> issue.getPriority().equals(priority))
                    .toList();
        }

        return issues;
    }

    public List<Issue> findByStatus(Status status) {
        if (status == null) {
            throw new InvalidEntityException("Status is null");
        }

        return this.findAll().stream().filter(issue -> issue.getStatus().equals(status)).toList();
    }

    public List<Issue> findByPriority(Priority priority) {
        if (priority == null) {
            throw new InvalidEntityException("Priority is null");
        }

        return this.findAll().stream().filter(issue -> issue.getPriority().equals(priority)).toList();
    }

    public List<Issue> findAssignTo(User user) {
        if (user == null) {
            throw new InvalidEntityException("User is null");
        }

        return this.findAll().stream().filter(issue -> user.equals(issue.getAssignedUser())).toList();
    }

    public Optional<Issue> findById(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidEntityException("Id is null");
        }

        return this.repository.findById(id);
    }

    public List<Issue> findAll() {

        return this.repository.findAll();
    }

    public Issue getIssueById(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        return issue.orElseThrow(() -> new IssueNotFoundException("Issue not found with id: " + id));
    }

    public List<Issue> orderByPriority() {
        return this.findAll().stream().sorted(Comparator.comparing(Issue::getPriority)).toList().reversed();
    }

    public List<Issue> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidEntityException("Title is null");
        }

        return this.findAll().stream()
                .filter(issue -> issue.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT).strip()))
                .toList();
    }

    // utils
    public long countOpenIssues() {

        return this.findAll().stream().filter(issue -> issue.getStatus().equals(Status.OPEN)).count();
    }

    // Issue Operations
    public void startIssue(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new IssueNotFoundException("Issue does not exist");
        }

        issue.get().start();
    }

    public void completeIssue(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new IssueNotFoundException("Issue does not exist");
        }

        issue.get().complete();
    }

    //User Operations
    public void assignUser(String id, User user) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new IssueNotFoundException("Issue does not exist");
        }
        if (user == null) {
            throw new InvalidEntityException("User is null");
        }

        issue.get().assignTo(user);
    }

    public void removeAssignedUser(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new IssueNotFoundException("Issue does not exist");
        }

        issue.get().removeAssignedUser();
    }
}
