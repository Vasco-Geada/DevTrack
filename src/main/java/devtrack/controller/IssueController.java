package devtrack.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import devtrack.dto.CreateTaskRequest;
import devtrack.dto.IssueResponse;
import devtrack.mapper.IssueMapper;
import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;
import devtrack.model.Issue;
import devtrack.model.Task;
import devtrack.service.IssueService;
import jakarta.validation.Valid;

@RequestMapping("api/issues")
@RestController
public class IssueController {

    private final IssueService issueService;
    private final IssueMapper issueMapper;

    public IssueController(
            IssueService issueService,
            IssueMapper issueMapper) {

        this.issueService = issueService;
        this.issueMapper = issueMapper;
    }

    /**
     * Get all issues, optionally filtered by status, priority, and query.
     *
     * @param status The status to filter by
     * @param priority The priority to filter by
     * @param query The query to search for in the issue titles
     * @return A list of issues matching the criteria
     */
    @GetMapping
    public ResponseEntity<List<IssueResponse>> getIssues(@RequestParam(name
            = "status", required = false) Status status,
            @RequestParam(name = "priority", required = false) Priority priority, @RequestParam(name = "q" /* title Query */, required = false) String query) {

        List<Issue> issues = issueService.findByStatusPriorityTitle(status, priority, query != null ? query : null);

        return ResponseEntity.status(200).body(issueMapper.toResponses(issues));
    }

    /**
     * Create a new issue.
     *
     * @param createTaskRequest The request containing the issue details
     * @return The created issue
     */
    @PostMapping
    public ResponseEntity<IssueResponse> createIssue(@Valid @RequestBody CreateTaskRequest createTaskRequest) {

        Task task = new Task(createTaskRequest.title(), createTaskRequest.description(), createTaskRequest.priority());

        issueService.createIssue(task);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(task.getId())
                .toUri();

        return ResponseEntity.created(location).body(issueMapper.toResponse(task));
    }

    /**
     * Get an issue by its ID.
     *
     * @param id The ID of the issue
     * @return The issue with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> getIssue(@PathVariable String id) {

        Issue issue = issueService.getIssueById(id);

        return ResponseEntity.status(200).body(issueMapper.toResponse(issue));
    }

    /**
     * Start an issue by its ID.
     *
     * @param id The ID of the issue
     * @return The started issue
     */
    @PatchMapping("/{id}/start")
    public ResponseEntity<IssueResponse> startIssue(@PathVariable String id) {

        issueService.startIssue(id);
        Issue issue = issueService.getIssueById(id);

        return ResponseEntity.status(200).body(issueMapper.toResponse(issue));
    }

    /**
     * Complete an issue by its ID.
     *
     * @param id The ID of the issue
     * @return The completed issue
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<IssueResponse> completeIssue(@PathVariable String id) {

        issueService.completeIssue(id);
        Issue issue = issueService.getIssueById(id);

        return ResponseEntity.status(200).body(issueMapper.toResponse(issue));
    }

    /**
     * Delete an issue by its ID.
     *
     * @param id The ID of the issue
     * @return A response indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable String id) {

        issueService.deleteIssue(id);

        return ResponseEntity.noContent().build();
    }
}
