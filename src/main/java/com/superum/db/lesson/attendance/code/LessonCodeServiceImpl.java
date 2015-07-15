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
	public List<LessonCode> add(LessonAttendance attendance, int partitionId) {
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
		
		return codeDAO.add(lessonCodes, partitionId);
	}
	
	@Override
	public int verifyStudentId(long lessonId, int code, int partitionId) {
		LOG.debug("Verifying code '{}' for Lesson '{}'", code, lessonId);
		
		int studentId = codeDAO.find(lessonId, code, partitionId);
		LOG.debug("The code belongs to Student with ID: {}", studentId);
		
		return studentId;
	}
	
	@Override
	public int deleteCodesForStudent(int studentId, int partitionId) {
		LOG.debug("Deleting LessonCodes for Student with ID: {}", studentId);
		
		int codeCount = codeDAO.deleteForStudent(studentId, partitionId);
		LOG.debug("LessonCodes deleted: {}", codeCount);
		
		return codeCount;
	}
	
	@Override
	public int deleteCodesForLesson(long lessonId, int partitionId) {
		LOG.debug("Deleting LessonCodes for Lesson with ID: {}", lessonId);
		
		int codeCount = codeDAO.deleteForLesson(lessonId, partitionId);
		LOG.debug("LessonCodes deleted: {}", codeCount);
		
		return codeCount;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonCodeServiceImpl(LessonCodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}

	// PRIVATE
	
	private final LessonCodeDAO codeDAO;
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonCodeService.class);

}
