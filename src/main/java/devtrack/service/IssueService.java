package devtrack.service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import devtrack.exception.InvalidEntityException;
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
            throw new InvalidEntityException("Issue does not exist");
        }

        this.repository.deleteById(id);
    }

    // Find 
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

    // Search & Order by
    public List<Issue> orderByPriority() {
        return this.findAll().stream().sorted(Comparator.comparing(Issue::getPriority)).toList().reversed();
    }

    public List<Issue> searchByTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new InvalidEntityException("Title is null");
        }

        return this.findAll().stream()
                .filter(issue -> issue.getTitle().toLowerCase(Locale.ROOT).contains(title.toLowerCase(Locale.ROOT)))
                .toList();
    }

    public long countOpenIssues() {

        return this.findAll().stream().filter(issue -> issue.getStatus().equals(Status.OPEN)).count();
    }

    // Issue Operations
    public void startIssue(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new InvalidEntityException("Issue does not exist");
        }

        issue.get().start();
    }

    public void completeIssue(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new InvalidEntityException("Issue does not exist");
        }

        issue.get().complete();
    }

    //User Operations
    public void assignUser(String id, User user) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new InvalidEntityException("Issue does not exist");
        }

        issue.get().assignTo(user);
    }

    public void removeAssignedUser(String id) {
        Optional<Issue> issue = this.repository.findById(id);

        if (issue.isEmpty()) {
            throw new InvalidEntityException("Issue does not exist");
        }

        issue.get().removeAssignedUser();
    }
}
