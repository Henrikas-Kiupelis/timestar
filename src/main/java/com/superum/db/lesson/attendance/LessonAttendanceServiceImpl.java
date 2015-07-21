package com.superum.db.lesson.attendance;

import com.superum.config.Gmail;
import com.superum.db.group.student.Student;
import com.superum.db.group.student.StudentDAO;
import com.superum.db.lesson.attendance.code.LessonCode;
import com.superum.db.lesson.attendance.code.LessonCodeDAO;
import com.superum.db.lesson.attendance.code.LessonCodeService;
import com.superum.db.partition.PartitionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Service
public class LessonAttendanceServiceImpl implements LessonAttendanceService {

	@Override
	public LessonAttendance addAttendanceToLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Adding attendance: {}", attendance);
		
		LessonAttendance newAttendance = attendanceDAO.create(attendance, partitionId);
		LOG.debug("Attendance added: {}", newAttendance);

        generateCode(newAttendance, partitionId);
		
		return newAttendance;
	}

	@Override
	public LessonAttendance getAttendanceForLesson(long lessonId, int partitionId) {
		LOG.debug("Reading LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance attendance = attendanceDAO.read(lessonId, partitionId);
		LOG.debug("LessonAttendance retrieved: {}", attendance);
		
		return attendance;
	}

	@Override
	public LessonAttendance updateAttendanceForLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Updating LessonAttendance: {}", attendance);
		
		LessonAttendance oldAttendance = attendanceDAO.update(attendance, partitionId);
		LOG.debug("Old LessonAttendance retrieved: {}", oldAttendance);
		
		return oldAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(long lessonId, int partitionId) {
		LOG.debug("Deleting LessonAttendance by ID: {}", lessonId);
		
		LessonAttendance deletedAttendance = attendanceDAO.delete(lessonId, partitionId);
		LOG.debug("Deleted LessonAttendance: {}", deletedAttendance);
		
		return deletedAttendance;
	}

	@Override
	public LessonAttendance deleteAttendanceForLesson(LessonAttendance attendance, int partitionId) {
		LOG.debug("Deleting LessonAttendance: {}", attendance);
		
		LessonAttendance attendanceBeforeDeletion = attendanceDAO.delete(attendance, partitionId);
		LOG.debug("Attendance before deletion: {}", attendanceBeforeDeletion);
		
		return attendanceBeforeDeletion;
	}
	
	@Override
	public int deleteAttendanceForStudent(int studentId, int partitionId) {
		LOG.debug("Deleting LessonAttendance for Student with ID: {}", studentId);
		
		int deletedAttendanceAmount = attendanceDAO.deleteForStudent(studentId, partitionId);
		LOG.debug("Deleted {} LessonAttendances", deletedAttendanceAmount);
		
		return deletedAttendanceAmount;
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceServiceImpl(LessonAttendanceDAO attendanceDAO, LessonCodeService lessonCodeService, LessonCodeDAO lessonCodeDAO, 
			StudentDAO studentDAO, Gmail mail, PartitionService partitionService) {
		this.attendanceDAO = attendanceDAO;
		this.lessonCodeService = lessonCodeService;
		this.lessonCodeDAO = lessonCodeDAO;
		this.studentDAO = studentDAO;
		this.mail = mail;
		this.partitionService = partitionService;
	}

	// PRIVATE
	
	private final LessonAttendanceDAO attendanceDAO;
	private final LessonCodeService lessonCodeService;
	private final LessonCodeDAO lessonCodeDAO;
	private final StudentDAO studentDAO;
	private final Gmail mail;
	private final PartitionService partitionService;

	private void generateCode(LessonAttendance newAttendance, int partitionId) {
        // To avoid long pauses when sending e-mails, accounts are created on a separate thread
        new Thread(() -> {
            String name = partitionService.findPartition(partitionId).getName();

            List<LessonCode> lessonCodes = lessonCodeService.add(newAttendance, partitionId);
            LOG.debug("Codes for students generated: {}", lessonCodes);

            for (LessonCode lessonCode : lessonCodes) {
                int studentId = lessonCode.getStudentId();
                int code = lessonCode.getCode();
                Student student = studentDAO.read(studentId, partitionId);
                try {
                    String fullTitle = EMAIL_TITLE + name;
                    String fullBody = EMAIL_BODY + code;
                    mail.send(student.getEmail(), fullTitle, fullBody);
                    LOG.debug("Sent email to student '{}': title - '{}'; body - '{}'", student, fullTitle, fullBody);
                } catch (MessagingException e) {
                    lessonCodeDAO.find(newAttendance.getLessonId(), code, partitionId);
                    throw new IllegalStateException("Failed to send mail! Code aborted.", e);
                }
            }
        }).run();
    }
	
	private static final String EMAIL_TITLE = "Your lesson code for ";
	private static final String EMAIL_BODY = "Code: ";
	
	private static final Logger LOG = LoggerFactory.getLogger(LessonAttendanceService.class);

}
