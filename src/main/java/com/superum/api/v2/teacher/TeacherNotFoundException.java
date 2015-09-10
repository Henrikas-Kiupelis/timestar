package com.superum.api.v2.teacher;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request for a teacher fails because the id used in the request
 * does not refer to any teachers
 */
public class TeacherNotFoundException extends DatabaseException {

	public TeacherNotFoundException() {
		super();
	}

	public TeacherNotFoundException(String message) {
		super(message);
	}

	public TeacherNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TeacherNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
