package com.superum.api.exception;

import com.superum.exception.DatabaseException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This exception is thrown when an invalid request is made, such as a negative ID value or a null object
 */
@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid parameter")
public class InvalidRequestException extends DatabaseException {

	public InvalidRequestException() {
		super();
	}

	public InvalidRequestException(String message) {
		super(message);
	}

	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestException(Throwable cause) {
		super(cause);
	}
	
}
