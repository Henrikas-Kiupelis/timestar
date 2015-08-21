package com.superum.api.lesson;

import com.superum.api.customer.InvalidCustomerException;
import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;

import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.LESSON;
import static com.superum.helper.validation.Validator.validate;

/**
 * <pre>
 * Domain object for lessons
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
public class ValidLesson extends MappedClass<ValidLesson, Long> {

    public ValidLesson(ValidLessonDTO validLessonDTO) {
        validate(validLessonDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson id must be positive, not: "+
                        validLessonDTO.getId()));

        validate(validLessonDTO.getGroupId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Group id for lesson must be positive, not: " +
                        validLessonDTO.getGroupId()));

        validate(validLessonDTO.getStartTime()).Null().or().equal(0).or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson start time must be non-negative, not: " +
                        validLessonDTO.getStartTime()));

        validate(validLessonDTO.getLength()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson length must be positive, not: " +
                        validLessonDTO.getLength()));

        validate(validLessonDTO.getComment()).Null().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidCustomerException("Lesson comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + validLessonDTO.getComment()));

        this.validLessonDTO = validLessonDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    private final ValidLessonDTO validLessonDTO;

    private static final int COMMENT_SIZE_LIMIT = 500;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String GROUP_ID_FIELD = "groupId";
    private static final String TEACHER_ID_FIELD = "teacherId";
    private static final String START_TIME_FIELD = "startTime";
    private static final String END_TIME_FIELD = "endTime";
    private static final String LENGTH_FIELD = "length";
    private static final String COMMENT_FIELD = "comment";

    // FIELD DEFINITIONS

    private static final List<FieldDef<ValidLesson, ?>> FIELD_DEFINITION_LIST = Arrays.asList(
            FieldDef.steps(ValidLesson.class, Long.class)
                    .fieldName(ID_FIELD)
                    .tableField(LESSON.ID)
                    .getter(lesson -> lesson.validLessonDTO.getId())
                    .primaryKey(),

            FieldDef.steps(ValidLesson.class, Integer.class)
                    .fieldName(GROUP_ID_FIELD)
                    .tableField(LESSON.GROUP_ID)
                    .getter(lesson -> lesson.validLessonDTO.getGroupId())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, Integer.class)
                    .fieldName(TEACHER_ID_FIELD)
                    .tableField(LESSON.TEACHER_ID)
                    .getter(lesson -> lesson.validLessonDTO.getTeacherId()),

            FieldDef.steps(ValidLesson.class, Long.class)
                    .fieldName(START_TIME_FIELD)
                    .tableField(LESSON.TIME_OF_START)
                    .getter(lesson -> lesson.validLessonDTO.getStartTime())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, Long.class)
                    .fieldName(END_TIME_FIELD)
                    .tableField(LESSON.TIME_OF_END)
                    .getter(lesson -> lesson.validLessonDTO.getEndTime())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, Integer.class)
                    .fieldName(LENGTH_FIELD)
                    .tableField(LESSON.DURATION_IN_MINUTES)
                    .getter(lesson -> lesson.validLessonDTO.getLength())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, String.class)
                    .fieldName(COMMENT_FIELD).tableField(LESSON.COMMENT)
                    .getter(lesson -> lesson.validLessonDTO.getComment())
    );

}
