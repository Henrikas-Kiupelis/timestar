package com.superum.db.lesson;

import static com.superum.db.generated.timestar.Tables.LESSON;

import java.beans.PropertyEditorSupport;

import org.jooq.TableField;

import com.superum.db.generated.timestar.tables.records.LessonRecord;

public enum TableBinder {

	teacher(LESSON.TEACHER_ID),
	customer(LESSON.CUSTOMER_ID),
	group(LESSON.GROUP_ID);
	
	// PUBLIC API
	
	public TableField<LessonRecord, Integer> field() {
		return field;
	}
	
	public static PropertyEditorSupport propertyEditorSupport() {
		return new TableBinderPropertyEditorSupport();
	}
	
	// CONSTRUCTORS/RETRIEVALS
	
	TableBinder(TableField<LessonRecord, Integer> field) {
		this.field = field;
	}
	
	// PRIVATE
	
	private final TableField<LessonRecord, Integer> field;
	
	private static class TableBinderPropertyEditorSupport extends PropertyEditorSupport {
		@Override
        public String getAsText() {
            return ((TableBinder) this.getValue()).toString();
        }

        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            setValue(TableBinder.valueOf(text));
        }
	}
	
}
