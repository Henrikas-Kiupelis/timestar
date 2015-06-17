package com.superum.db.lesson.attendance;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class AttendanceController {

	@RequestMapping(value = "/lesson/attendance/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Attendance addAttendanceToLesson(@RequestBody @Valid Attendance attendance) {
		return attendanceService.addAttendanceToLesson(attendance);
	}
	
	@RequestMapping(value = "/lesson/attendance/{lessonId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Attendance getAttendanceForLesson(@PathVariable long lessonId) {
		return attendanceService.getAttendanceForLesson(lessonId);
	}
	
	@RequestMapping(value = "/lesson/attendance/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Attendance updateAttendanceForLesson(@RequestBody @Valid Attendance attendance) {
		return attendanceService.updateAttendanceForLesson(attendance);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete/{lessonId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Attendance deleteAttendanceForLesson(@PathVariable long lessonId) {
		return attendanceService.deleteAttendanceForLesson(lessonId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Attendance deleteAttendanceForLesson(@RequestBody @Valid Attendance attendance) {
		return attendanceService.deleteAttendanceForLesson(attendance);
	}

	// CONSTRUCTORS

	@Autowired
	public AttendanceController(AttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	// PRIVATE
	
	private final AttendanceService attendanceService;

}
