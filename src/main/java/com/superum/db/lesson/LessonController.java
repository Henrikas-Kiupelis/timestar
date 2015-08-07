package com.superum.db.lesson;

import com.superum.utils.PrincipalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonController {

	@RequestMapping(value = "/lesson/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson addLesson(Principal user, @RequestBody @Valid Lesson lesson) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.addLesson(lesson, partitionId);
	}
	
	@RequestMapping(value = "/lesson/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson findLesson(Principal user, @PathVariable long id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLesson(id, partitionId);
	}
	
	@RequestMapping(value = "/lesson/update", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson updateLesson(Principal user, @RequestBody @Valid Lesson lesson) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.updateLesson(lesson, partitionId);
	}
	
	@RequestMapping(value = "/lesson/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Lesson deleteLesson(Principal user, @PathVariable long id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.deleteLesson(id, partitionId);
	}
	
	@RequestMapping(value = "/lesson/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForTeacher(Principal user, @PathVariable int teacherId,
											  @RequestParam(value="start", required=false) Date start,
											  @RequestParam(value="end", required=false) Date end) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLessonsForTeacher(teacherId, start, end, partitionId);
	}
	
	@RequestMapping(value = "/lesson/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForCustomer(Principal user, @PathVariable int customerId,
											   @RequestParam(value="start", required=false) Date start,
											   @RequestParam(value="end", required=false) Date end) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLessonsForCustomer(customerId, start, end, partitionId);
	}
	
	@RequestMapping(value = "/lesson/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForGroup(Principal user, @PathVariable int groupId,
											@RequestParam(value="start", required=false) Date start,
											@RequestParam(value="end", required=false) Date end) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLessonsForGroup(groupId, start, end, partitionId);
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}
	
	// PRIVATE
	
	private final LessonService lessonService;
	
}
