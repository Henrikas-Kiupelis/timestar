package com.superum.api.teacher;

import com.superum.exception.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when the a request for a Teacher fails because the id used in the request
 * does not refer to any Teachers
 * </pre>
 */
@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Cannot find the specified Teacher")
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
