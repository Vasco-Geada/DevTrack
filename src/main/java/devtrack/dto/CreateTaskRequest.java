package devtrack.dto;

import devtrack.model.Enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(@NotBlank
        @NotNull
        String title, @NotNull
        String description, @NotNull
        Priority priority) {}
