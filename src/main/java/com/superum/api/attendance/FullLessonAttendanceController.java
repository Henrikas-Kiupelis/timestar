package com.superum.api.attendance;

import com.superum.db.group.student.Student;
import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.lesson.attendance.LessonAttendanceController;
import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api/v2/lesson/attendance")
public class FullLessonAttendanceController {

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<Student> addAttendanceToLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
        return fullLessonAttendanceService.addAttendanceAndReturnStudents(attendance, account.partitionId());
    }

    @RequestMapping(value = "/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance getAttendanceForLesson(PartitionAccount account, @PathVariable long lessonId) {
        return lessonAttendanceController.getAttendanceForLesson(account, lessonId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance updateAttendanceForLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
        return lessonAttendanceController.updateAttendanceForLesson(account, attendance);
    }

    @RequestMapping(value = "/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance deleteAttendanceForLesson(PartitionAccount account, @PathVariable long lessonId) {
        return lessonAttendanceController.deleteAttendanceForLesson(account, lessonId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance deleteAttendanceForLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
        return lessonAttendanceController.deleteAttendanceForLesson(account, attendance);
    }

    // CONSTRUCTORS

    @Autowired
    public FullLessonAttendanceController(LessonAttendanceController lessonAttendanceController, FullLessonAttendanceService fullLessonAttendanceService) {
        this.lessonAttendanceController = lessonAttendanceController;
        this.fullLessonAttendanceService = fullLessonAttendanceService;
    }

    // PRIVATE

    private final LessonAttendanceController lessonAttendanceController;
    private final FullLessonAttendanceService fullLessonAttendanceService;

}
