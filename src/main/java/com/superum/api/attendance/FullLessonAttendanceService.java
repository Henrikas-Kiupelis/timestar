package com.superum.api.attendance;

import com.superum.db.group.student.Student;
import com.superum.db.lesson.attendance.LessonAttendance;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface FullLessonAttendanceService {

    List<Student> addAttendanceAndReturnStudents(LessonAttendance attendance, int partitionId);

}
