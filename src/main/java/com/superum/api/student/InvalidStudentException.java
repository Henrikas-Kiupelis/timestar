package com.superum.api.student;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of student
 * does not match any of the use cases
 * </pre>
 */
public class InvalidStudentException extends DatabaseException {

	public InvalidStudentException() {
		super();
	}

	public InvalidStudentException(String message) {
		super(message);
	}

	public InvalidStudentException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidStudentException(Throwable cause) {
		super(cause);
	}
	
}
