package com.superum.db.lesson;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonController {

	@RequestMapping(value = "/lesson/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson addLesson(@RequestBody @Valid Lesson lesson) {
		return lessonService.addLesson(lesson);
	}
	
	@RequestMapping(value = "/lesson/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson findLesson(@PathVariable long id) {
		return lessonService.findLesson(id);
	}
	
	@RequestMapping(value = "/lesson/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson updateLesson(@RequestBody @Valid Lesson lesson) {
		return lessonService.updateLesson(lesson);
	}
	
	@RequestMapping(value = "/lesson/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson deleteLesson(@PathVariable long id) {
		return lessonService.deleteLesson(id);
	}
	
	@RequestMapping(value = "/lesson/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Lesson> findLessonsForTeacher(@PathVariable int teacherId,
											  @RequestParam(value="start", required=false) Date start,
											  @RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForTeacher(teacherId, start, end);
	}
	
	@RequestMapping(value = "/lesson/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Lesson> findLessonsForCustomer(@PathVariable int customerId,
											   @RequestParam(value="start", required=false) Date start,
											   @RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForCustomer(customerId, start, end);
	}
	
	@RequestMapping(value = "/lesson/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Lesson> findLessonsForGroup(@PathVariable int groupId,
											@RequestParam(value="start", required=false) Date start,
											@RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsForGroup(groupId, start, end);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}
	
	// PRIVATE
	
	private final LessonService lessonService;
	
}
