package devtrack.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import devtrack.dto.CreateTaskRequest;
import devtrack.dto.IssueResponse;
import devtrack.mapper.IssueMapper;
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

    @GetMapping
    public ResponseEntity<List<IssueResponse>> getIssues() {

        List<Issue> issues = issueService.findAll();

        return ResponseEntity.status(200).body(issueMapper.toResponses(issues));
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<IssueResponse> getIssue(@PathVariable String id) {

        Optional<Issue> issue = issueService.getIssueById(id);

        if (issue.isPresent()) {
            return ResponseEntity.status(200).body(issueMapper.toResponse(issue.get()));
        }

        return ResponseEntity.status(404).build();
    }
}
