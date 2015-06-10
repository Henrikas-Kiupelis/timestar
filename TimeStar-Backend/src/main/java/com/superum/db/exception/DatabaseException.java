package com.superum.db.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception to be thrown when we are certain that a {@code User} doesn't exist<p>
 * 
 * i.e. when a {@code userId} value doesn't refer to any existing records
 * 
 * @author Goodlike
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Failed to access database")
public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = -6192053563692323344L;

	public DatabaseException() {
		super();
	}
	
	public DatabaseException(String message) {
		super(message);
	}
	
	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DatabaseException(Throwable cause) {
		super(cause);
	}
	
}
