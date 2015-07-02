package com.superum.db.teacher.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class TeacherLanguagesController {

	@RequestMapping(value = "/teacher/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages addLanguagesToTeacher(@RequestBody @Valid TeacherLanguages languages) {
		return languageService.addLanguagesToTeacher(languages);
	}
	
	@RequestMapping(value = "/teacher/lang/{teacherId:[\\d]+}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages getLanguagesForTeacher(@PathVariable int teacherId) {
		return languageService.getLanguagesForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/teacher/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages updateLanguagesForTeacher(@RequestBody @Valid TeacherLanguages languages) {
		return languageService.updateLanguagesForTeacher(languages);
	}
	
	@RequestMapping(value = "/teacher/lang/delete/{teacherId:[\\d]+}", method = RequestMethod.DELETE, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(@PathVariable int teacherId) {
		return languageService.deleteLanguagesForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public TeacherLanguages deleteLanguagesForTeacher(@RequestBody @Valid TeacherLanguages languages) {
		return languageService.deleteLanguagesForTeacher(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesController(TeacherLanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final TeacherLanguagesService languageService;

}
