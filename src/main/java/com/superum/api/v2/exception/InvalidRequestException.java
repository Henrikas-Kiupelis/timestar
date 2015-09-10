package com.superum.api.v2.exception;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, such as a negative ID value or a null object
 *
 * This exception is now final, because extending it causes its ExceptionHandler to be called over the subclasses,
 * giving the wrong error message/code in certain scenarios
 * </pre>
 */
public final class InvalidRequestException extends DatabaseException {

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
