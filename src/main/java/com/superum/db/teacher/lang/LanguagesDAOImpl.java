package com.superum.db.teacher.lang;

import static com.superum.db.generated.timestar.Tables.TEACHER_LANGUAGE;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.TeacherLanguageRecord;

@Repository
@Transactional
public class LanguagesDAOImpl implements LanguagesDAO {

	@Override
	public Languages create(Languages languages) {
		Integer teacherId = languages.getTeacherId();
		List<String> languageList = languages.getLanguages();
		
		InsertValuesStep2<TeacherLanguageRecord, Integer, String> step = sql.insertInto(TEACHER_LANGUAGE, TEACHER_LANGUAGE.TEACHER_ID, TEACHER_LANGUAGE.CODE);
		for (String language : languageList)
			step = step.values(teacherId, language);
		
		step.execute();
		return languages;
	}

	@Override
	public Languages read(Integer teacherId) {
		List<String> languages =  sql.select(TEACHER_LANGUAGE.CODE)
				.from(TEACHER_LANGUAGE)
				.where(TEACHER_LANGUAGE.TEACHER_ID.eq(teacherId))
				.fetch()
				.map(record -> record.getValue(TEACHER_LANGUAGE.CODE));
		return new Languages(teacherId, languages);
	}

	@Override
	public Languages update(Languages languages) {
		Languages old = delete(languages.getTeacherId());
		
		create(languages);
		return old;
	}

	@Override
	public Languages delete(Integer teacherId) {
		return delete(new Languages(teacherId, Collections.emptyList()));
	}

	@Override
	public Languages delete(Languages languages) {
		Integer teacherId = languages.getTeacherId();
		
		Languages old = read(teacherId);
		
		List<String> languageList = languages.getLanguages();
		
		Condition condition = TEACHER_LANGUAGE.TEACHER_ID.eq(teacherId);
		for (String language : languageList)
			condition = condition.and(TEACHER_LANGUAGE.CODE.eq(language));
		
		sql.delete(TEACHER_LANGUAGE)
			.where(condition)
			.execute();
		
		return old;
	}
	
	// CONSTRUCTORS

	@Autowired
	public LanguagesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
