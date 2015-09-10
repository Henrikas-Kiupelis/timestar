package com.superum.api.v2.lesson;

import com.superum.exception.DatabaseException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of lesson
 * does not match any of the use cases
 */
public class InvalidLessonException extends DatabaseException {

	public InvalidLessonException() {
		super();
	}

	public InvalidLessonException(String message) {
		super(message);
	}

	public InvalidLessonException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidLessonException(Throwable cause) {
		super(cause);
	}
	
}
