package com.superum.api.attendance;

import com.superum.api.student.FullStudentService;
import com.superum.db.group.student.Student;
import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.lesson.attendance.LessonAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FullLessonAttendanceServiceImpl implements FullLessonAttendanceService {

    @Override
    public List<Student> addAttendanceAndReturnStudents(LessonAttendance attendance, int partitionId) {
        LessonAttendance createdLessonAttendance = lessonAttendanceService.addAttendanceToLesson(attendance, partitionId);
        return fullStudentService.readAllStudentsForIds(createdLessonAttendance.getStudentIds(), partitionId);
    }

    // CONSTRUCTORS

    @Autowired
    public FullLessonAttendanceServiceImpl(LessonAttendanceService lessonAttendanceService, FullStudentService fullStudentService) {
        this.lessonAttendanceService = lessonAttendanceService;
        this.fullStudentService = fullStudentService;
    }

    // PRIVATE

    private final LessonAttendanceService lessonAttendanceService;
    private final FullStudentService fullStudentService;

}
