package com.superum.api.teacher;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when an invalid Teacher creation request is made, specifically when the email of the given
 * teacher is already taken
 * </pre>
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="A teacher with specified email already exists")
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
