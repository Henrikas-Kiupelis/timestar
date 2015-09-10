package com.superum.api.v2.lesson;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when the a request for a lesson fails because the id used in the request
 * does not refer to any lessons
 */
public class LessonNotFoundException extends DatabaseException {

	public LessonNotFoundException() {
		super();
	}

	public LessonNotFoundException(String message) {
		super(message);
	}

	public LessonNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public LessonNotFoundException(Throwable cause) {
		super(cause);
	}
	
}
