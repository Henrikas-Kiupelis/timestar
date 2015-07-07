package com.superum.db.lesson.attendance;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonAttendanceController {

	@RequestMapping(value = "/lesson/attendance/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance addAttendanceToLesson(@RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.addAttendanceToLesson(attendance);
	}
	
	@RequestMapping(value = "/lesson/attendance/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance getAttendanceForLesson(@PathVariable long lessonId) {
		return attendanceService.getAttendanceForLesson(lessonId);
	}
	
	@RequestMapping(value = "/lesson/attendance/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance updateAttendanceForLesson(@RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.updateAttendanceForLesson(attendance);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(@PathVariable long lessonId) {
		return attendanceService.deleteAttendanceForLesson(lessonId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(@RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.deleteAttendanceForLesson(attendance);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceController(LessonAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	// PRIVATE
	
	private final LessonAttendanceService attendanceService;

}
