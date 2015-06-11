package com.superum.db.lesson;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class LessonController {

	@RequestMapping(value = "/lesson/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Lesson addLesson(@RequestBody @Valid Lesson lesson) {
		return lessonService.addLesson(lesson);
	}
	
	@RequestMapping(value = "/lesson/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Lesson findLesson(@PathVariable long id) {
		return lessonService.findLesson(id);
	}
	
	@RequestMapping(value = "/lesson/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Lesson updateLesson(@RequestBody @Valid Lesson lesson) {
		return lessonService.updateLesson(lesson);
	}
	
	@RequestMapping(value = "/lesson/delete/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Lesson deleteLesson(@PathVariable long id) {
		return lessonService.deleteLesson(id);
	}
	
	@RequestMapping(value = "/lesson/{table}/{id}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public List<Lesson> findLessonsFor(@PathVariable TableBinder table, @PathVariable int id,
										@RequestParam(value="start", required=false) Date start,
										@RequestParam(value="end", required=false) Date end) {
		return lessonService.findLessonsFor(table, id, start, end);
	}
	
	// BINDERS
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(TableBinder.class, TableBinder.propertyEditorSupport());
	}
	
	// CONSTRUCTORS
	
	@Autowired
	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}
	
	// PRIVATE
	
	private final LessonService lessonService;

}
