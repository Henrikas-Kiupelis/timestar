package com.superum.api.group;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of group
 * does not match any of the use cases
 */
public class InvalidGroupException extends DatabaseException {

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
