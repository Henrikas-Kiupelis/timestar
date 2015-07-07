package com.superum.db.lesson.attendance.code;

import com.superum.utils.RandomUtils;
import com.superum.utils.StringUtils;

public class LessonCode {

	public long getLessonId() {
		return lessonId;
	}

	public int getStudentId() {
		return studentId;
	}

	public int getCode() {
		return code;
	}
	
	public static int generateCode() {
		return RandomUtils.randomNumber(900000) + 100000;
	}

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return StringUtils.toString(
					"Lesson ID: " + lessonId,
					"Student ID: " + studentId,
					"Code: " + code);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof LessonCode))
			return false;

		LessonCode other = (LessonCode) o;

		return this.lessonId == other.lessonId
				&& this.studentId == other.studentId
				&& this.code == other.code;
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = (result << 5) - result + (int)lessonId;
		result = (result << 5) - result + studentId;
		result = (result << 5) - result + code;
		return result;
	}

	// CONSTRUCTORS

	public LessonCode(long lessonId, int studentId, int code) {
		this.lessonId = lessonId;
		this.studentId = studentId;
		this.code = code;
	}

	// PRIVATE
	
	private final long lessonId;
	
	private final int studentId;
	
	private final int code;

}
