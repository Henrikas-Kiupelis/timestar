package com.superum.api.group;

import com.superum.api.exception.InvalidRequestException;

/**
 * <pre>
 * This exception is thrown when an invalid request is made, specifically when the format of Group
 * does not match any of the use cases
 * </pre>
 */
public class InvalidGroupException extends InvalidRequestException {

	public InvalidGroupException() {
		super();
	}

	public InvalidGroupException(String message) {
		super(message);
	}

	public InvalidGroupException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidGroupException(Throwable cause) {
		super(cause);
	}
	
}
