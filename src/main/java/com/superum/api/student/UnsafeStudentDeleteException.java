package com.superum.api.student;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request to delete a student fails because this student still has
 * references in other database objects
 * </pre>
 */
public class UnsafeStudentDeleteException extends DatabaseException {

	public UnsafeStudentDeleteException() {
		super();
	}

	public UnsafeStudentDeleteException(String message) {
		super(message);
	}

	public UnsafeStudentDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeStudentDeleteException(Throwable cause) {
		super(cause);
	}
	
}
