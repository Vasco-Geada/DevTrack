package devtrack.exception;

public class InvalidIssueStateException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	public InvalidIssueStateException(String errorMessage) {
		super(errorMessage);
	}
	public InvalidIssueStateException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
