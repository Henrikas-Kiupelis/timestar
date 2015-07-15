package com.superum.db.lesson;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.security.Principal;
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

import com.superum.utils.PrincipalUtils;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonController {

	@RequestMapping(value = "/lesson/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson addLesson(Principal user, @RequestBody @Valid Lesson lesson) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.addLesson(lesson, partitionId);
	}
	
	@RequestMapping(value = "/lesson/{id:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson findLesson(Principal user, @PathVariable long id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLesson(id, partitionId);
	}
	
	@RequestMapping(value = "/lesson/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson updateLesson(Principal user, @RequestBody @Valid Lesson lesson) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.updateLesson(lesson, partitionId);
	}
	
	@RequestMapping(value = "/lesson/delete/{id:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Lesson deleteLesson(Principal user, @PathVariable long id) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.deleteLesson(id, partitionId);
	}
	
	@RequestMapping(value = "/lesson/teacher/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Lesson> findLessonsForTeacher(Principal user, @PathVariable int teacherId,
											  @RequestParam(value="start", required=false) Date start,
											  @RequestParam(value="end", required=false) Date end) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLessonsForTeacher(teacherId, start, end, partitionId);
	}
	
	@RequestMapping(value = "/lesson/customer/{customerId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public List<Lesson> findLessonsForCustomer(Principal user, @PathVariable int customerId,
											   @RequestParam(value="start", required=false) Date start,
											   @RequestParam(value="end", required=false) Date end) {
		int partitionId = PrincipalUtils.partitionId(user);
		return lessonService.findLessonsForCustomer(customerId, start, end, partitionId);
	}
	
	@RequestMapping(value = "/lesson/group/{groupId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
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
