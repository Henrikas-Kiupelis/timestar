package com.superum.api.v2.teacher;

/**
 * This exception is thrown when an invalid teacher creation request is made, specifically when the email of the given
 * teacher is already taken
 */
public class DuplicateTeacherException extends InvalidTeacherException {

	public DuplicateTeacherException() {
		super();
	}

	public DuplicateTeacherException(String message) {
		super(message);
	}

	public DuplicateTeacherException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateTeacherException(Throwable cause) {
		super(cause);
	}
	
}
