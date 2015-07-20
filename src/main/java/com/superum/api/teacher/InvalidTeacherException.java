package com.superum.api.teacher;

import com.superum.api.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of Teacher
 * does not match any of the use cases
 * </pre>
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid Teacher format, check your JSON")
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
