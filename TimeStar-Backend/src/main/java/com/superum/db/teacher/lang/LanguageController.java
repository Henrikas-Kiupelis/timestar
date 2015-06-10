package com.superum.db.teacher.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class LanguageController {

	@RequestMapping(value = "/api/teacher/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Languages addLanguagesToTeacher(@RequestBody @Valid Languages languages) {
		return languageService.addLanguagesToTeacher(languages);
	}
	
	@RequestMapping(value = "/api/teacher/lang/get", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Languages getLanguagesForTeacher(@RequestParam(value="id") int teacherId) {
		return languageService.getLanguagesForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/api/teacher/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Languages deleteLanguagesForTeacher(@RequestBody @Valid Languages languages) {
		return languageService.deleteLanguagesForTeacher(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public LanguageController(LanguageService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final LanguageService languageService;

}
