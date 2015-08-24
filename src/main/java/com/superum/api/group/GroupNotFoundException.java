package com.superum.api.group;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request for a group fails because the id used in the request
 * does not refer to any groups
 */
public class GroupNotFoundException extends DatabaseException {

	public GroupNotFoundException() {
		super();
	}

	public GroupNotFoundException(String message) {
		super(message);
	}

	public GroupNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public GroupNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
