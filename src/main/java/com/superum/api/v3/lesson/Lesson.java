package com.superum.api.v3.lesson;

import com.google.common.base.MoreObjects;
import com.superum.api.v2.lesson.OverlappingLessonException;
import eu.goodlike.neat.Null;
import org.jooq.Record;
import org.springframework.dao.DataAccessException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * <pre>
 * Domain object representing a lesson
 *
 * If a lesson needs to operated on (updated or created in some way), the relevant code should be in this class
 *
 * If a lesson needs to be queried, prefer using FetchedLesson or SQL queries directly
 *
 * These lessons should be already validated against the database, that is, their insertion/update should not result
 * in database exceptions, unless the database changes in the time between the creation and consumption of this object;
 * however, these lessons might still not be inserted/updated due to overlapping with other existing lessons (which is
 * a logical constraint, not a database one)
 * </pre>
 */
public final class Lesson {

    /**
     * Creates a new lesson in the database
     * @return the created lesson
     * @throws OverlappingLessonException if this lesson overlaps with others for its teacher
     * @throws DataAccessException if an unexpected database error occurred
     */
    public Optional<FetchedLesson> create(int partitionId, Function<Record, FetchedLesson> mapper) {
        Null.check(lessonRepository).ifAny("Lesson repository was not initialized for lesson!");

        if (lessonRepository.isOverlapping(teacherId, startTime, endTime, partitionId))
            throw new OverlappingLessonException("This teacher already has a lesson during this time, cannot create!");

        return lessonRepository.insert(groupId, teacherId, startTime, endTime, length, comment, partitionId, mapper);
    }

    /**
     * Updates a lesson in the database
     * @throws OverlappingLessonException if this lesson overlaps with others for its teacher
     * @throws DataAccessException if an unexpected database error occurred
     */
    public int update(long lessonId, int partitionId) {
        Null.check(lessonRepository).ifAny("Lesson repository was not initialized for lesson!");

        if (lessonRepository.isOverlapping(lessonId, teacherId, startTime, endTime, partitionId))
            throw new OverlappingLessonException("This teacher already has a lesson during this time, cannot update!");

        return lessonRepository.update(lessonId, groupId, teacherId, startTime, endTime, length, comment, partitionId);
    }

    // CONSTRUCTORS

    public Lesson(int groupId, int teacherId, long startTime, long endTime, int length, String comment,
                  LessonRepository lessonRepository) {
        this.groupId = groupId;
        this.teacherId = teacherId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.length = length;
        this.comment = comment;

        this.lessonRepository = lessonRepository;
    }

    // PRIVATE

    private final int groupId;
    private final int teacherId;
    private final long startTime;
    private final long endTime;
    private final int length;
    private final String comment;

    private final LessonRepository lessonRepository;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("groupId", groupId)
                .add("teacherId", teacherId)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("length", length)
                .add("comment", comment)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lesson)) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(groupId, lesson.groupId) &&
                Objects.equals(teacherId, lesson.teacherId) &&
                Objects.equals(startTime, lesson.startTime) &&
                Objects.equals(endTime, lesson.endTime) &&
                Objects.equals(length, lesson.length) &&
                Objects.equals(comment, lesson.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, teacherId, startTime, endTime, length, comment);
    }

}
