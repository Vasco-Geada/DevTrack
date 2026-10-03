package devtrack.mapper;

import java.util.List;

import devtrack.dto.IssueResponse;
import devtrack.exception.InvalidEntityException;
import devtrack.model.Enums.IssueType;
import devtrack.model.Issue;

public class IssueMapper {

    public IssueResponse toResponse(Issue issue) {
        if (issue == null) {
            throw new InvalidEntityException("Issue is null");
        }

        return new IssueResponse(
                issue.getId(),
                issue.getTitle(),
                issue.getDescription(),
                issue.getStatus(),
                issue.getPriority(),
                checkType(issue),
                issue.getAssignedUser() == null ? null : issue.getAssignedUser().getId(),
                issue.getAssignedUser() == null ? null : issue.getAssignedUser().getName(),
                issue.getCreatedAt(),
                issue.getUpdatedAt()
        );
    }

    public List<IssueResponse> toResponses(List<Issue> issues) {
        if (issues == null) {
            throw new InvalidEntityException("Issue is null");
        }

        return issues.stream()
                .map(this::toResponse).toList();
    }

    private IssueType checkType(Issue issue) {
        if (!(issue instanceof Issue)) {
            throw new IllegalArgumentException("Unknown issue type: " + issue.getClass().getSimpleName());
        }

        switch (issue.getClass().getSimpleName()) {
            case "Bug" -> {
                return IssueType.BUG;
            }
            case "Feature" -> {
                return IssueType.FEATURE;
            }
            case "Task" -> {
                return IssueType.TASK;
            }
            default ->
                throw new IllegalArgumentException("Unknown issue type: " + issue.getClass().getSimpleName());
        }

    }
}
