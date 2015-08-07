package com.superum.db.group.student;

import com.superum.db.lesson.attendance.LessonAttendanceService;
import com.superum.db.partition.PartitionService;
import com.superum.helper.mail.GMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

	@Override
	public Student addStudent(Student student, int partitionId) {
		LOG.debug("Creating new Student: {}", student);
		
		Student newStudent = studentDAO.create(student, partitionId);
		LOG.debug("New Student created: {}", newStudent);

        generateCode(newStudent, partitionId);
		
		return newStudent;
	}

	@Override
	public Student findStudent(int id, int partitionId) {
		LOG.debug("Reading Student by ID: {}", id);
		
		Student student = studentDAO.read(id, partitionId);
		LOG.debug("Student retrieved: {}");
		
		return student;
	}

	@Override
	public Student updateStudent(Student student, int partitionId) {
		LOG.debug("Updating Student: {}", student);
		
		Student oldStudent = studentDAO.update(student, partitionId);
		LOG.debug("Old Student retrieved: {}", oldStudent);
		
		return oldStudent;
	}

	@Override
	public Student deleteStudent(int id, int partitionId) {
		LOG.debug("Deleting Student by ID: {}", id);
		
		int deletedAttendanceCount = attendanceService.deleteAttendanceForStudent(id, partitionId);
		LOG.debug("Deleted {} Student attendance records", deletedAttendanceCount);
		
		Student deletedStudent = studentDAO.delete(id, partitionId);
		LOG.debug("Deleted Student: {}", deletedStudent);
		
		return deletedStudent;
	}

	@Override
	public List<Student> findStudentsForCustomer(int customerId, int partitionId) {
		LOG.debug("Reading Students for Customer with ID: {}", customerId);
		
		List<Student> studentsForCustomer = studentDAO.readAllForCustomer(customerId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForCustomer);
		
		return studentsForCustomer;
	}

	@Override
	public List<Student> findStudentsForGroup(int groupId, int partitionId) {
		LOG.debug("Reading Students for Group with ID: {}", groupId);
		
		List<Student> studentsForGroup = studentQueries.readAllForGroup(groupId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForGroup);
		
		return studentsForGroup;
	}
	
	@Override
	public List<Student> findStudentsForLesson(long lessonId, int partitionId) {
		LOG.debug("Reading Students for Lesson with ID: {}", lessonId);
		
		List<Student> studentsForLesson = studentQueries.readAllForLesson(lessonId, partitionId);
		LOG.debug("Students retrieved: {}", studentsForLesson);
		
		return studentsForLesson;
	}
	
	@Override
	public List<Student> deleteForCustomer(int customerId, int partitionId) {
		LOG.debug("Deleting Students for Customer with ID: {}", customerId);
		
		List<Student> old = findStudentsForCustomer(customerId, partitionId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(studentId -> deleteStudent(studentId, partitionId));
		LOG.debug("Deleted Students: {}", old);
		
		return old;
	}
	
	@Override
	public List<Student> deleteForGroup(int groupId, int partitionId) {
		LOG.debug("Deleting Students for Group with ID: {}", groupId);
		
		List<Student> old = findStudentsForGroup(groupId, partitionId);
		
		old.stream()
			.mapToInt(Student::getId)
			.forEach(studentId -> deleteStudent(studentId, partitionId));
		LOG.debug("Deleted Students: {}", old);
		
		return old;
	}

	// CONSTRUCTORS

	@Autowired
	public StudentServiceImpl(StudentDAO studentDAO, StudentQueries studentQueries, LessonAttendanceService attendanceService, GMail mail, PartitionService partitionService) {
		this.studentDAO = studentDAO;
		this.studentQueries = studentQueries;
		this.attendanceService = attendanceService;
        this.mail = mail;
        this.partitionService = partitionService;
	}

	// PRIVATE
	
	private final StudentDAO studentDAO;
	private final StudentQueries studentQueries;
	private final LessonAttendanceService attendanceService;
	private final GMail mail;
	private final PartitionService partitionService;

    private void generateCode(Student student, int partitionId) {
        // To avoid long pauses when sending e-mails/generating numbers, a separate thread is used
        new Thread(() -> {
            String name = partitionService.findPartition(partitionId).getName();

            Student studentWithCode = student.withGeneratedCode();
            LOG.debug("Code for student generated: {}", studentWithCode);

			studentDAO.setStudentCode(studentWithCode.getId(), studentWithCode.getCode(), partitionId);
            try {
                String fullTitle = EMAIL_TITLE + name;
                String fullBody = EMAIL_BODY + studentWithCode.getCode();
                mail.send(student.getEmail(), fullTitle, fullBody);
                LOG.debug("Sent email to student '{}': title - '{}'; body - '{}'", student.getName(), fullTitle, fullBody);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static final String EMAIL_TITLE = "Your lesson code for ";
    private static final String EMAIL_BODY = "Code: ";

    private static final Logger LOG = LoggerFactory.getLogger(StudentService.class);

}
