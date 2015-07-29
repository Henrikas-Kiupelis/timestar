package com.superum.api.exception;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when an invalid request is made, such as a negative ID value or a null object
 */
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
