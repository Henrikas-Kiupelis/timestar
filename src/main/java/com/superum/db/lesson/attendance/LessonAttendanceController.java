package com.superum.db.lesson.attendance;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonAttendanceController {

	@RequestMapping(value = "/lesson/attendance/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance addAttendanceToLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.addAttendanceToLesson(attendance, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/attendance/{lessonId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance getAttendanceForLesson(PartitionAccount account, @PathVariable long lessonId) {
		return attendanceService.getAttendanceForLesson(lessonId, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/attendance/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance updateAttendanceForLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.updateAttendanceForLesson(attendance, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/attendance/delete/{lessonId:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(PartitionAccount account, @PathVariable long lessonId) {
		return attendanceService.deleteAttendanceForLesson(lessonId, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/attendance/delete", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public LessonAttendance deleteAttendanceForLesson(PartitionAccount account, @RequestBody @Valid LessonAttendance attendance) {
		return attendanceService.deleteAttendanceForLesson(attendance, account.partitionId());
	}

	// CONSTRUCTORS

	@Autowired
	public LessonAttendanceController(LessonAttendanceService attendanceService) {
		this.attendanceService = attendanceService;
	}

	// PRIVATE
	
	private final LessonAttendanceService attendanceService;

}
