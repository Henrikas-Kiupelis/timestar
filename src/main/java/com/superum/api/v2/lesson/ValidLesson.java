package com.superum.api.v2.lesson;

import com.superum.exception.DatabaseException;
import com.superum.helper.field.MappedClass;
import com.superum.helper.field.steps.FieldDef;
import eu.goodlike.validation.Validate;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.jooq.Condition;
import org.jooq.DSLContext;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static timestar_v2.Tables.GROUP_OF_STUDENTS;
import static timestar_v2.Tables.LESSON;

/**
 * <pre>
 * Domain object for lessons
 *
 * This object should be used to validate DTO data and use it in a meaningful manner; it encapsulates only the
 * specific version of DTO, which is used for commands
 * </pre>
 */
@SuppressWarnings("deprecation")
public class ValidLesson extends MappedClass<ValidLesson, Long> {

    public static long calculateEndTime(long startTime, int length) {
        return new Instant(startTime).plus(Duration.standardMinutes(length)).getMillis();
    }

    public Long calculateEndTime() {
        Long startTime = validLessonDTO.getStartTime();
        Integer length = validLessonDTO.getLength();
        return startTime == null || length == null
                ? null
                : calculateEndTime(startTime, length);
    }

    public boolean isOverlapping(DSLContext sql, int partitionId) {
        Long startTime = validLessonDTO.getStartTime();
        Integer length = validLessonDTO.getLength();
        if (startTime == null && length == null)
            return false;

        if (startTime == null)
            startTime = findStartTime(sql, partitionId);
        if (length == null)
            length = findLength(sql, partitionId);

        Long endTime = calculateEndTime(startTime, length);

        Integer teacherId = validLessonDTO.getTeacherId();
        if (teacherId == null)
            teacherId = findTeacherId(sql, partitionId);

        Condition aLessonForSameTeacher = LESSON.TEACHER_ID.eq(teacherId)
                .and(LESSON.PARTITION_ID.eq(partitionId));

        Condition aLessonStartsBetweenThisLesson = LESSON.TIME_OF_START.between(startTime, endTime);
        Condition aLessonEndsBetweenThisLesson = LESSON.TIME_OF_END.between(startTime, endTime);
        Condition thisLessonStartsBetweenALesson = LESSON.TIME_OF_START.le(startTime)
                .and(LESSON.TIME_OF_END.ge(startTime));
        // No need to check for end time, because it is automatically caught by the first two conditions as well

        Condition lessonIsOverlapping = aLessonForSameTeacher
                .and(aLessonStartsBetweenThisLesson
                        .or(aLessonEndsBetweenThisLesson)
                        .or(thisLessonStartsBetweenALesson));

        if (hasId())
            lessonIsOverlapping = LESSON.ID.ne(getId()).and(lessonIsOverlapping);

        return sql.fetchExists(LESSON, lessonIsOverlapping);
    }

    public boolean hasNonExistentGroupId(Predicate<Integer> groupIdCheck) {
        return validLessonDTO.getGroupId() != null && groupIdCheck.test(validLessonDTO.getGroupId());
    }

    // CONSTRUCTORS

    public ValidLesson(ValidLessonDTO validLessonDTO) {
        Validate.Long(validLessonDTO.getId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson id must be positive, not: "+
                        validLessonDTO.getId()));

        Validate.Int(validLessonDTO.getGroupId()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Group id for lesson must be positive, not: " +
                        validLessonDTO.getGroupId()));

        Validate.Long(validLessonDTO.getStartTime()).Null().or().equal(0).or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson start time must be non-negative, not: " +
                        validLessonDTO.getStartTime()));

        Validate.Int(validLessonDTO.getLength()).Null().or().moreThan(0)
                .ifInvalid(() -> new InvalidLessonException("Lesson length must be positive, not: " +
                        validLessonDTO.getLength()));

        Validate.string(validLessonDTO.getComment()).Null().or().fits(COMMENT_SIZE_LIMIT)
                .ifInvalid(() -> new InvalidLessonException("Lesson comment must not exceed " +
                        COMMENT_SIZE_LIMIT + " chars: " + validLessonDTO.getComment()));

        this.validLessonDTO = validLessonDTO;

        registerFields(FIELD_DEFINITION_LIST, this);
    }

    private final ValidLessonDTO validLessonDTO;

    private long findStartTime(DSLContext sql, int partitionId) {
        return sql.select(LESSON.TIME_OF_START)
                .from(LESSON)
                .where(LESSON.ID.eq(validLessonDTO.getId())
                        .and(LESSON.PARTITION_ID.eq(partitionId)))
                .fetch().stream().findAny()
                .map(record -> record.getValue(LESSON.TIME_OF_START))
                .orElseThrow(() -> new DatabaseException("Problem retrieving lesson start time for id: "
                        + validLessonDTO.getId()));
    }

    private int findLength(DSLContext sql, int partitionId) {
        return sql.select(LESSON.DURATION_IN_MINUTES)
                .from(LESSON)
                .where(LESSON.ID.eq(validLessonDTO.getId())
                        .and(LESSON.PARTITION_ID.eq(partitionId)))
                .fetch().stream().findAny()
                .map(record -> record.getValue(LESSON.DURATION_IN_MINUTES))
                .orElseThrow(() -> new DatabaseException("Problem retrieving lesson duration for id: "
                        + validLessonDTO.getId()));
    }

    private int findTeacherId(DSLContext sql, int partitionId) {
        Integer groupId = validLessonDTO.getGroupId();
        return groupId == null
                ? sql.select(LESSON.TEACHER_ID)
                .from(LESSON)
                .where(LESSON.ID.eq(validLessonDTO.getId())
                        .and(LESSON.PARTITION_ID.eq(partitionId)))
                .fetch().stream().findAny()
                .map(record -> record.getValue(LESSON.TEACHER_ID))
                .orElseThrow(() -> new DatabaseException("Problem retrieving teacher id for lesson with id: "
                        + validLessonDTO.getId()))
                : sql.select(GROUP_OF_STUDENTS.TEACHER_ID)
                .from(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.ID.eq(groupId)
                        .and(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId)))
                .fetch().stream().findAny()
                .map(record -> record.getValue(GROUP_OF_STUDENTS.TEACHER_ID))
                .orElseThrow(() -> new DatabaseException("Problem retrieving teacher id for specified group id: "
                        + validLessonDTO.getGroupId()));
    }

    private static final int COMMENT_SIZE_LIMIT = 500;

    // FIELD NAMES

    private static final String ID_FIELD = "id";
    private static final String GROUP_ID_FIELD = "groupId";
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

            FieldDef.steps(ValidLesson.class, Long.class)
                    .fieldName(START_TIME_FIELD)
                    .tableField(LESSON.TIME_OF_START)
                    .getter(lesson -> lesson.validLessonDTO.getStartTime())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, Long.class)
                    .fieldName(END_TIME_FIELD)
                    .tableField(LESSON.TIME_OF_END)
                    .getter(ValidLesson::calculateEndTime)
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, Integer.class)
                    .fieldName(LENGTH_FIELD)
                    .tableField(LESSON.DURATION_IN_MINUTES)
                    .getter(lesson -> lesson.validLessonDTO.getLength())
                    .mandatory(),

            FieldDef.steps(ValidLesson.class, String.class)
                    .fieldName(COMMENT_FIELD)
                    .tableField(LESSON.COMMENT)
                    .getter(lesson -> lesson.validLessonDTO.getComment())
    );

}
