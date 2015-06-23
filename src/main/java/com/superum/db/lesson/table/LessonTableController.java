package com.superum.db.lesson.table;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.superum.db.lesson.table.core.LessonTable;

@Controller
@RequestMapping(value = "/timestar/api")
public class LessonTableController {

	@RequestMapping(value = "/lesson/table", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public LessonTable lessonData(@RequestParam(value="start", required=false) Date start,
								  @RequestParam(value="end", required=false) Date end) {
		return lessonTableService.lessonData(start, end);
	}

	// CONSTRUCTORS

	@Autowired
	public LessonTableController(LessonTableService lessonTableService) {
		this.lessonTableService = lessonTableService;
	}

	// PRIVATE
	
	private final LessonTableService lessonTableService;

}
