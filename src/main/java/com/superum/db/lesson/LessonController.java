package com.superum.db.lesson;

import com.superum.helper.PartitionAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonController {

	@RequestMapping(value = "/lesson/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson addLesson(PartitionAccount account, @RequestBody @Valid Lesson lesson) {
		return lessonService.addLesson(lesson, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson findLesson(PartitionAccount account, @PathVariable long id) {
		return lessonService.findLesson(id, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson updateLesson(PartitionAccount account, @RequestBody @Valid Lesson lesson) {
		return lessonService.updateLesson(lesson, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson deleteLesson(PartitionAccount account, @PathVariable long id) {
		return lessonService.deleteLesson(id, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForTeacher(PartitionAccount account, @PathVariable int teacherId,
											  @RequestParam(value="start", required=false) Date start,
											  @RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForTeacher(teacherId, start, end, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForCustomer(PartitionAccount account, @PathVariable int customerId,
											   @RequestParam(value="start", required=false) Date start,
											   @RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForCustomer(customerId, start, end, account.partitionId());
	}
	
	@RequestMapping(value = "/lesson/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForGroup(PartitionAccount account, @PathVariable int groupId,
											@RequestParam(value="start", required=false) Date start,
											@RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForGroup(groupId, start, end, account.partitionId());
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}
	
	// PRIVATE
	
	private final LessonService lessonService;
	
}
