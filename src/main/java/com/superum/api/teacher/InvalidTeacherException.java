package com.superum.api.teacher;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of teacher
 * does not match any of the use cases
 */
public class InvalidTeacherException extends DatabaseException {

	public InvalidTeacherException() {
		super();
	}

	public InvalidTeacherException(String message) {
		super(message);
	}

	public InvalidTeacherException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidTeacherException(Throwable cause) {
		super(cause);
	}
	
}
