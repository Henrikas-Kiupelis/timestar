package com.superum.api.attendance;

import com.superum.api.teacher.InvalidTeacherException;

/**
 * This exception is thrown when creating lesson attendance fails because it already exists
 */
public class DuplicateLessonAttendanceException extends InvalidTeacherException {

	public DuplicateLessonAttendanceException() {
		super();
	}

	public DuplicateLessonAttendanceException(String message) {
		super(message);
	}

	public DuplicateLessonAttendanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicateLessonAttendanceException(Throwable cause) {
		super(cause);
	}
	
}
