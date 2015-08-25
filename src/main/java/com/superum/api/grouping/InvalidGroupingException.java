package com.superum.api.grouping;

import com.superum.api.exception.InvalidRequestException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of grouping
 * does not match any of the use cases
 * </pre>
 */
public class InvalidGroupingException extends InvalidRequestException {

	public InvalidGroupingException() {
		super();
	}

	public InvalidGroupingException(String message) {
		super(message);
	}

	public InvalidGroupingException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidGroupingException(Throwable cause) {
		super(cause);
	}
	
}
