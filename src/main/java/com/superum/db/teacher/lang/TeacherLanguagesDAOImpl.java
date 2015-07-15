package com.superum.db.teacher.lang;

import static com.superum.db.generated.timestar.Tables.TEACHER_LANGUAGE;

import java.util.Collections;
import java.util.List;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.InsertValuesStep3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.superum.db.generated.timestar.tables.records.TeacherLanguageRecord;

@Repository
@Transactional
public class TeacherLanguagesDAOImpl implements TeacherLanguagesDAO {

	@Override
	public TeacherLanguages create(TeacherLanguages languages, int partitionId) {
		Integer teacherId = languages.getTeacherId();
		List<String> languageList = languages.getLanguages();
		
		InsertValuesStep3<TeacherLanguageRecord, Integer, Integer, String> step = sql.insertInto(TEACHER_LANGUAGE, TEACHER_LANGUAGE.PARTITION_ID, TEACHER_LANGUAGE.TEACHER_ID, TEACHER_LANGUAGE.CODE);
		for (String language : languageList)
			step = step.values(partitionId, teacherId, language);
		
		step.execute();
		return languages;
	}

	@Override
	public TeacherLanguages read(Integer teacherId, int partitionId) {
		List<String> languages =  sql.select(TEACHER_LANGUAGE.CODE)
				.from(TEACHER_LANGUAGE)
				.where(TEACHER_LANGUAGE.TEACHER_ID.eq(teacherId)
						.and(TEACHER_LANGUAGE.PARTITION_ID.eq(partitionId)))
				.fetch()
				.map(record -> record.getValue(TEACHER_LANGUAGE.CODE));
		return new TeacherLanguages(teacherId, languages);
	}

	@Override
	public TeacherLanguages update(TeacherLanguages languages, int partitionId) {
		TeacherLanguages old = delete(languages.getTeacherId(), partitionId);
		
		create(languages, partitionId);
		return old;
	}

	@Override
	public TeacherLanguages delete(Integer teacherId, int partitionId) {
		return delete(new TeacherLanguages(teacherId, Collections.emptyList()), partitionId);
	}

	@Override
	public TeacherLanguages delete(TeacherLanguages languages, int partitionId) {
		Integer teacherId = languages.getTeacherId();
		
		TeacherLanguages old = read(teacherId, partitionId);
		
		List<String> languageList = languages.getLanguages();
		
		Condition condition = TEACHER_LANGUAGE.TEACHER_ID.eq(teacherId)
				.and(TEACHER_LANGUAGE.PARTITION_ID.eq(partitionId));
		for (String language : languageList)
			condition = condition.and(TEACHER_LANGUAGE.CODE.eq(language));
		
		sql.delete(TEACHER_LANGUAGE)
			.where(condition)
			.execute();
		
		return old;
	}
	
	// CONSTRUCTORS

	@Autowired
	public TeacherLanguagesDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
