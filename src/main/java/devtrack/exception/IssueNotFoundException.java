package devtrack.exception;

public class IssueNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 3L;

    public IssueNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
