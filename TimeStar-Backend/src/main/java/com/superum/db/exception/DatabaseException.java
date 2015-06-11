package com.superum.db.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Placeholder exception for all database problems at the current time
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
