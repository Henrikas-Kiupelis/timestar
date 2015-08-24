package com.superum.api.teacher;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request to delete a teacher fails because this teacher still has
 * references in other database objects
 */
public class UnsafeTeacherDeleteException extends DatabaseException {

	public UnsafeTeacherDeleteException() {
		super();
	}

	public UnsafeTeacherDeleteException(String message) {
		super(message);
	}

	public UnsafeTeacherDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeTeacherDeleteException(Throwable cause) {
		super(cause);
	}
	
}
