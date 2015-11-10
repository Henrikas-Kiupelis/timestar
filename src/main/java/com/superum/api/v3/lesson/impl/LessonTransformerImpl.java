package com.superum.api.v3.lesson.impl;

import com.superum.api.v2.group.GroupNotFoundException;
import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v3.lesson.Lesson;
import com.superum.api.v3.lesson.LessonRepository;
import com.superum.api.v3.lesson.LessonTransformer;
import com.superum.api.v3.lesson.dto.FetchedLesson;
import com.superum.api.v3.lesson.dto.SuppliedLesson;
import com.superum.api.v3.lesson.dto.SuppliedLessonWithTimestamp;
import com.superum.api.v3.lesson.dto.SuppliedLessonWithTimezone;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.time.Time;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import timestar_v2.tables.records.GroupOfStudentsRecord;
import timestar_v2.tables.records.LessonRecord;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.MINUTES;
import static timestar_v2.Tables.GROUP_OF_STUDENTS;
import static timestar_v2.Tables.LESSON;

@Service
public class LessonTransformerImpl implements LessonTransformer {

    @Override
    public Lesson from(SuppliedLessonWithTimestamp lessonToCreate) {
        lessonToCreate.validateForCreation();

        int groupId = lessonToCreate.getGroupId();
        long startTime = lessonToCreate.getStartTime();
        int length = lessonToCreate.getLength();

        int teacherId = getTeacherId(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Couldn't find group id for lesson: " + lessonToCreate));

        long endTime = getEndTime(startTime, length);

        return Lesson.valueOf(groupId, teacherId, startTime, endTime, length, lessonToCreate.getComment(), lessonRepository);
    }

    @Override
    public Lesson from(SuppliedLessonWithTimestamp lessonToUpdate, long lessonId) {
        lessonToUpdate.validateForUpdate();

        Integer groupId = lessonToUpdate.getGroupId();
        Integer teacherId = null;
        if (groupId != null)
            teacherId = getTeacherId(groupId)
                    .orElseThrow(() -> new GroupNotFoundException("Couldn't find group id for lesson: " + lessonToUpdate));

        Long startTime = lessonToUpdate.getStartTime();
        Integer length = lessonToUpdate.getLength();
        String comment = lessonToUpdate.getComment();

        FetchedLesson currentLesson = lessonQueries.read(lessonId, this::from)
                .orElseThrow(() -> new LessonNotFoundException("Couldn't find lesson with id: " + lessonId));

        if (groupId == null) {
            groupId = currentLesson.getGroupId();
            teacherId = currentLesson.getTeacherId();
        }
        if (startTime == null) startTime = currentLesson.getStartTime();
        if (length == null) length = currentLesson.getLength();
        if (comment == null) comment = currentLesson.getComment();

        long endTime = getEndTime(startTime, length);

        return Lesson.valueOf(groupId, teacherId, startTime, endTime, length, comment, lessonRepository);
    }

    @Override
    public SuppliedLessonWithTimestamp from(SuppliedLessonWithTimezone lesson, boolean toCreate) {
        if (toCreate)
            lesson.validateForCreation();
        else
            lesson.validateForUpdate();

        Long startTime = null;
        if (lesson.getTimezone() != null) {
            ZoneId timezone = lesson.getTimezone();
            LocalDate startDate = lesson.getStartDate();
            int hour = lesson.getStartHour();
            int minute = lesson.getStartMinute();

            startTime = Time.at(timezone).from(startDate, hour, minute).toEpochMilli();
        }

        return SuppliedLessonWithTimestamp.stepBuilder()
                .withGroupId(lesson.getGroupId())
                .withStartTime(startTime)
                .withLength(lesson.getLength())
                .withComment(lesson.getComment())
                .build();
    }

    @Override
    public SuppliedLessonWithTimestamp from(SuppliedLesson lesson, boolean toCreate) {
        return lesson.getStartTime() == null
                ? from(lesson.timezoneOnly(), toCreate)
                : lesson.timestampOnly();
    }

    @Override
    public FetchedLesson from(Record record) {
        if (record == null)
            return null;

        return FetchedLesson.stepBuilder()
                .withId(record.getValue(LESSON.ID))
                .withGroupId(record.getValue(LESSON.GROUP_ID))
                .withTeacherId(record.getValue(LESSON.TEACHER_ID))
                .withStartTime(record.getValue(LESSON.TIME_OF_START))
                .withEndTime(record.getValue(LESSON.TIME_OF_END))
                .withLength(record.getValue(LESSON.DURATION_IN_MINUTES))
                .withComment(record.getValue(LESSON.COMMENT))
                .withCreatedAt(record.getValue(LESSON.CREATED_AT))
                .withUpdatedAt(record.getValue(LESSON.UPDATED_AT))
                .build();
    }

    // CONSTRUCTORS

    @Autowired
    public LessonTransformerImpl(Queries<LessonRecord, Long> lessonQueries,
                                 Queries<GroupOfStudentsRecord, Integer> groupQueries,
                                 LessonRepository lessonRepository) {
        this.lessonQueries = lessonQueries;
        this.groupQueries = groupQueries;
        this.lessonRepository = lessonRepository;
    }

    // PRIVATE

    private final Queries<LessonRecord, Long> lessonQueries;
    private final Queries<GroupOfStudentsRecord, Integer> groupQueries;
    private final LessonRepository lessonRepository;

    private Optional<Integer> getTeacherId(int groupId) {
        return groupQueries.readField(GROUP_OF_STUDENTS.TEACHER_ID, groupId);
    }

    private long getEndTime(long startTime, int length) {
        return Instant.ofEpochMilli(startTime).plus(length, MINUTES).toEpochMilli();
    }

}
