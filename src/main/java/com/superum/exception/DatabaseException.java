package com.superum.exception;

/**
 * Placeholder exception for all database problems at the current time
 * 
 * @author Goodlike
 */
public class DatabaseException extends RuntimeException {

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
