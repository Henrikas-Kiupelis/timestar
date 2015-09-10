package com.superum.api.v2.lesson;

/**
 * This exception is thrown when a request to create or update a lesson is made, but the lesson overlaps with existing
 * lessons for this teacher
 */
public class OverlappingLessonException extends InvalidLessonException {

	public OverlappingLessonException() {
		super();
	}

	public OverlappingLessonException(String message) {
		super(message);
	}

	public OverlappingLessonException(String message, Throwable cause) {
		super(message, cause);
	}

	public OverlappingLessonException(Throwable cause) {
		super(cause);
	}
	
}
