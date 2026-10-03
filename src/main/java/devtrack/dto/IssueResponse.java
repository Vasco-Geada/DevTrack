package devtrack.dto;

import java.time.LocalDateTime;

import devtrack.model.Enums.IssueType;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;

public record IssueResponse(String id, String title, String description, Status status, Priority priority, IssueType type,
        String assignedUserId, String assignedUserName, LocalDateTime createdAt, LocalDateTime updatedAt) {

}
