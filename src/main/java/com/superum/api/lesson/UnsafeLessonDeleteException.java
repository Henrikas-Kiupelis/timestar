package com.superum.api.lesson;

import com.superum.exception.DatabaseException;

/**
 * <pre>
 * This exception is thrown when the a request to delete a lesson fails because this lesson still has
 * references in other database objects
 * </pre>
 */
public class UnsafeLessonDeleteException extends DatabaseException {

	public UnsafeLessonDeleteException() {
		super();
	}

	public UnsafeLessonDeleteException(String message) {
		super(message);
	}

	public UnsafeLessonDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnsafeLessonDeleteException(Throwable cause) {
		super(cause);
	}
	
}
