package com.superum.api.lesson;

import com.superum.api.exception.InvalidRequestException;

/**
 * This exception is thrown when an invalid request is made, specifically when the format of lesson
 * does not match any of the use cases
 */
public class InvalidLessonException extends InvalidRequestException {

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
