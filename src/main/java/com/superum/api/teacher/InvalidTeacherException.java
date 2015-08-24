package com.superum.api.teacher;

import com.superum.api.exception.InvalidRequestException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of teacher
 * does not match any of the use cases
 */
public class InvalidTeacherException extends InvalidRequestException {

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
