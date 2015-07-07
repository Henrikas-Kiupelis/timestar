package com.superum.db.lesson.attendance.code;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superum.db.lesson.attendance.LessonAttendance;

@Service
public class LessonCodeServiceImpl implements LessonCodeService {

	@Override
	public List<LessonCode> add(LessonAttendance attendance) {
		LOG.debug("Creating codes for attendance: {}", attendance);
		
		long lessonId = attendance.getLessonId();
		List<Integer> studentIds = attendance.getStudentIds();

		//The following code ensures that all the codes are unique for this lesson
		Set<Integer> codes = new HashSet<Integer>();
		while (codes.size() < studentIds.size())
			codes.add(LessonCode.generateCode());
		LOG.debug("Generated these codes: {}", codes);
		
		List<LessonCode> lessonCodes = new ArrayList<>();
		int i = 0;
		for (int code : codes)
			lessonCodes.add(new LessonCode(lessonId, studentIds.get(i++), code));
		LOG.debug("Assigned the codes to students: {}", lessonCodes);
		
		return codeDAO.add(lessonCodes);
	}
	
	@Override
	public int verifyStudentId(long lessonId, int code) {
		return codeDAO.find(lessonId, code);
	}
	
	@Override
	public int deleteCodesForStudent(int studentId) {
		return codeDAO.deleteForStudent(studentId);
	}
	
	@Override
	public int deleteCodesForLesson(long lessonId) {
		return codeDAO.deleteForLesson(lessonId);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonCodeServiceImpl(LessonCodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}

	// PRIVATE
	
	private final LessonCodeDAO codeDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonCodeServiceImpl.class);

}
