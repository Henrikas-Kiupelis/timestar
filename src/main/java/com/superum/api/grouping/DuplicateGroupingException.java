package com.superum.api.grouping;

import com.superum.api.teacher.InvalidTeacherException;

/**
 * This exception is thrown when creating grouping fails because it already exists
 */
public class DuplicateGroupingException extends InvalidTeacherException {

	public DuplicateGroupingException() {
		super();
	}

	public DuplicateGroupingException(String message) {
		super(message);
	}

	public DuplicateGroupingException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateGroupingException(Throwable cause) {
		super(cause);
	}
	
}
