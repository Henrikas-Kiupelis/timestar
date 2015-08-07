package com.superum.exception;

/**
 * <pre>
 * This exception is thrown when an error occurs inside the database, i.e. malformed SQL statements, key constraint
 * violations and similar
 * </pre>
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
