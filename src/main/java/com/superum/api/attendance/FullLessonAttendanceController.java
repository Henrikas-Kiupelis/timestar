package com.superum.api.attendance;

import com.superum.db.group.student.Student;
import com.superum.db.lesson.attendance.LessonAttendance;
import com.superum.db.lesson.attendance.LessonAttendanceController;
import com.superum.helper.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static com.superum.helper.utils.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api/v2/lesson/attendance")
public class FullLessonAttendanceController {

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public List<Student> addAttendanceToLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
        int partitionId = PrincipalUtils.partitionId(user);
        return fullLessonAttendanceService.addAttendanceAndReturnStudents(attendance, partitionId);
    }

    @RequestMapping(value = "/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance getAttendanceForLesson(Principal user, @PathVariable long lessonId) {
        return lessonAttendanceController.getAttendanceForLesson(user, lessonId);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance updateAttendanceForLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
        return lessonAttendanceController.updateAttendanceForLesson(user, attendance);
    }

    @RequestMapping(value = "/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance deleteAttendanceForLesson(Principal user, @PathVariable long lessonId) {
        return lessonAttendanceController.deleteAttendanceForLesson(user, lessonId);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public LessonAttendance deleteAttendanceForLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
        return lessonAttendanceController.deleteAttendanceForLesson(user, attendance);
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
