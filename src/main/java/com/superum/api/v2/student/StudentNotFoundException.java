package com.superum.api.v2.student;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request for a student fails because the id used in the request
 * does not refer to any students
 * </pre>
 */
public class StudentNotFoundException extends DatabaseException {

	public StudentNotFoundException() {
		super();
	}

	public StudentNotFoundException(String message) {
		super(message);
	}

	public StudentNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public StudentNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
