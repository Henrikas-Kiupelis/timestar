package com.superum.db.lesson;

import com.superum.helper.PartitionAccount;
import com.superum.helper.time.TimeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

	@RequestMapping(value = "/lesson/{table:teacher|customer|group}/{id:[\\d]+}", method = RequestMethod.GET, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public List<Lesson> findLessonsForTable(PartitionAccount account, @PathVariable String table, @PathVariable int id,
                                              @RequestParam(value="timeZone", required=false) String timeZone,
                                              @RequestParam(value="startDate", required=false) String startDate,
                                              @RequestParam(value="endDate", required=false) String endDate,
                                              @RequestParam(value="start", required=false) Long start,
											  @RequestParam(value="end", required=false) Long end) {
        TimeResolver timeResolver = TimeResolver.from(timeZone, startDate, endDate, start, end);
        return lessonService.findLessonsForTable(table, id, timeResolver.getStartTime(), timeResolver.getEndTime(), account.partitionId());
	}

	// CONSTRUCTORS
	
	@Autowired
	public LessonController(LessonService lessonService) {
		this.lessonService = lessonService;
	}
	
	// PRIVATE
	
	private final LessonService lessonService;
	
}
