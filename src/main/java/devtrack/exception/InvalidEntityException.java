package devtrack.exception;

public class InvalidEntityException extends RuntimeException  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidEntityException(String errorMessage) {
		super(errorMessage);
	}
	public InvalidEntityException(String errorMessage, Throwable err) {
		super(errorMessage, err);
	}
}
