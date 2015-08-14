package com.superum.api.group;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request to delete a group fails because this group still has
 * references in other database objects
 * </pre>
 */
public class UnsafeGroupDeleteException extends DatabaseException {

	public UnsafeGroupDeleteException() {
		super();
	}

	public UnsafeGroupDeleteException(String message) {
		super(message);
	}

	public UnsafeGroupDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeGroupDeleteException(Throwable cause) {
		super(cause);
	}
	
}
