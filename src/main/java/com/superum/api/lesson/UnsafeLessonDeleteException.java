package com.superum.api.lesson;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request to delete a lesson fails because this lesson still has
 * references in other database objects
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
