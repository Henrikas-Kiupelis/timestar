package com.superum.db.lesson.attendance;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonAttendanceController {

	@RequestMapping(value = "/lesson/attendance/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance addAttendanceToLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.addAttendanceToLesson(attendance, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance getAttendanceForLesson(Principal user, @PathVariable long lessonId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.getAttendanceForLesson(lessonId, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance updateAttendanceForLesson(Principal user, @RequestBody @Valid LessonAttendance attendance) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.updateAttendanceForLesson(attendance, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(Principal user, @PathVariable long lessonId) {
		int partitionId = PrincipalUtils.partitionId(user);
		return attendanceService.deleteAttendanceForLesson(lessonId, partitionId);
	}
	
	@RequestMapping(value = "/lesson/attendance/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
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
