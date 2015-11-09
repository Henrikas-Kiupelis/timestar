package com.superum.api.v3.lesson.impl;

import com.superum.api.v2.lesson.LessonNotFoundException;
import com.superum.api.v2.lesson.UnsafeLessonDeleteException;
import com.superum.api.v3.lesson.Lesson;
import com.superum.api.v3.lesson.LessonCommands;
import com.superum.api.v3.lesson.LessonRepository;
import com.superum.api.v3.lesson.LessonTransformer;
import com.superum.api.v3.lesson.dto.FetchedLesson;
import com.superum.api.v3.lesson.dto.SuppliedLessonWithTimestamp;
import com.superum.exception.DatabaseException;
import eu.goodlike.libraries.jooq.Queries;
import eu.goodlike.libraries.jooq.QueriesForeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import timestar_v2.tables.records.LessonRecord;

@Service
@Transactional
public class LessonCommandsImpl implements LessonCommands {

    @Override
    public FetchedLesson create(SuppliedLessonWithTimestamp suppliedLesson, int partitionId) {
        Lesson lesson = lessonTransformer.from(suppliedLesson);
        return lesson.create(partitionId, lessonTransformer::from)
                .orElseThrow(() -> new DatabaseException("Couldn't return lesson after inserting it: " + lesson));
    }

    @Override
    public void update(long lessonId, SuppliedLessonWithTimestamp suppliedLesson, int partitionId) {
        Lesson lesson = lessonTransformer.from(suppliedLesson, lessonId);
        if (lesson.update(lessonId, partitionId) == 0)
            throw new DatabaseException("Couldn't update lesson: " + lesson);
    }

    @Override
    public void delete(long lessonId, int partitionId) {
        if (!lessonQueries.exists(lessonId))
            throw new LessonNotFoundException("Couldn't find lesson with id " + lessonId);

        if (lessonForeignQueries.isUsed(lessonId))
            throw new UnsafeLessonDeleteException("Cannot delete lesson with id " + lessonId +
                    " while it still has entries in other tables");

        if (lessonRepository.delete(lessonId) == 0)
            throw new DatabaseException("Couldn't delete lesson with id: " + lessonId);
    }

    // CONSTRUCTORS

    @Autowired
    public LessonCommandsImpl(LessonTransformer lessonTransformer, LessonRepository lessonRepository,
                              Queries<LessonRecord, Long> lessonQueries, QueriesForeign<Long> lessonForeignQueries) {
        this.lessonTransformer = lessonTransformer;
        this.lessonRepository = lessonRepository;
        this.lessonQueries = lessonQueries;
        this.lessonForeignQueries = lessonForeignQueries;
    }

    // PRIVATE

    private final LessonTransformer lessonTransformer;
    private final LessonRepository lessonRepository;
    private final Queries<LessonRecord, Long> lessonQueries;
    private final QueriesForeign<Long> lessonForeignQueries;

}
