package com.superum.db.teacher.lang;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class LanguagesController {

	@RequestMapping(value = "/teacher/lang/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Languages addLanguagesToTeacher(@RequestBody @Valid Languages languages) {
		return languageService.addLanguagesToTeacher(languages);
	}
	
	@RequestMapping(value = "/teacher/lang/{teacherId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Languages getLanguagesForTeacher(@PathVariable int teacherId) {
		return languageService.getLanguagesForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/teacher/lang/update", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Languages updateLanguagesForTeacher(@RequestBody @Valid Languages languages) {
		return languageService.updateLanguagesForTeacher(languages);
	}
	
	@RequestMapping(value = "/teacher/lang/delete/{teacherId}", method = RequestMethod.GET, produces = RETURN_CONTENT_TYPE)
	public Languages deleteLanguagesForTeacher(@PathVariable int teacherId) {
		return languageService.deleteLanguagesForTeacher(teacherId);
	}
	
	@RequestMapping(value = "/teacher/lang/delete", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	public Languages deleteLanguagesForTeacher(@RequestBody @Valid Languages languages) {
		return languageService.deleteLanguagesForTeacher(languages);
	}

	// CONSTRUCTORS

	@Autowired
	public LanguagesController(LanguagesService languageService) {
		this.languageService = languageService;
	}

	// PRIVATE
	
	private final LanguagesService languageService;

}
