package devtrack.dto;

import java.util.Map;

import org.springframework.http.HttpStatus;

public record ApiErrorResponse(HttpStatus status, String message, String path, Map<String, String> fieldErrors) {}
