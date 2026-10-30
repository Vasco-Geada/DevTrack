package devtrack.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import devtrack.dto.ApiErrorResponse;
import devtrack.exception.InvalidIssueStateException;
import devtrack.exception.IssueNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

/**
 * ApiExceptionHandler
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> invalidDataException(MethodArgumentNotValidException exception, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid data", request.getRequestURI(), errors);

        return ResponseEntity.status(422).body(response);
    }

    @ExceptionHandler(IssueNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(IssueNotFoundException exception, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        errors.put("path", request.getRequestURI());

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request.getRequestURI(), errors);

        return ResponseEntity.status(404).body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException exception, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        errors.put("path", request.getRequestURI());

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI(), errors);

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentTypeMismatch(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        errors.put("path", request.getRequestURI());

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request.getRequestURI(), errors);

        return ResponseEntity.status(400).body(response);
    }

    @ExceptionHandler(InvalidIssueStateException.class)
    public ResponseEntity<ApiErrorResponse> handleArgumentTypeMismatch(InvalidIssueStateException exception, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", exception.getMessage());
        errors.put("path", request.getRequestURI());

        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT, exception.getMessage(), request.getRequestURI(), errors);

        return ResponseEntity.status(409).body(response);
    }
}
