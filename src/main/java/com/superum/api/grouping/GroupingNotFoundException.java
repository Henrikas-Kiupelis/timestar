package com.superum.api.grouping;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when grouping cannot be found
 */
public class GroupingNotFoundException extends DatabaseException {

	public GroupingNotFoundException() {
		super();
	}

	public GroupingNotFoundException(String message) {
		super(message);
	}

	public GroupingNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupingNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
