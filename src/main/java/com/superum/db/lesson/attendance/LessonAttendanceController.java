package com.superum.db.lesson.attendance;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonAttendanceController {

	@RequestMapping(value = "/lesson/attendance/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance addAttendanceToLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.addAttendanceToLesson(attendance, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance getAttendanceForLesson(Principal user, @PathVariable long lessonId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.getAttendanceForLesson(lessonId, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance updateAttendanceForLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.updateAttendanceForLesson(attendance, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(Principal user, @PathVariable long lessonId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.deleteAttendanceForLesson(lessonId, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.deleteAttendanceForLesson(attendance, partitionId);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceController(LessonAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	// PRIVATE
	
	private final LessonAttendanceService attendanceService;

}
