package devtrack.dto;

import java.util.Map;

import ch.qos.logback.core.status.Status;

public record ApiErrorResponse(Status status, String message, String path, Map<String, String> fieldErrors) {}
