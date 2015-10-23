package IT.com.superum.api.v3;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.v3.lesson.FetchedLesson;
import com.superum.api.v3.lesson.SuppliedLesson;
import com.superum.helper.Fakes;
import eu.goodlike.libraries.spring.mockmvc.MVC;
import eu.goodlike.test.Fake;
import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

import static eu.goodlike.libraries.spring.mockmvc.HttpResult.*;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class LessonControllerIT extends IntegrationTestEnvironment {

    @Test
    public void creatingLessonWithoutId_shouldCreateNewLesson() throws Exception {
        SuppliedLesson lesson = Fakes.suppliedLesson(NEW_LESSON_ID, OLD_GROUP_ID);

        FetchedLesson insertedLesson = mvc.performPut(DEFAULT_PATH, lesson, OK)
                .map(Unchecked.function(this::readLesson))
                .orElseThrow(() -> new Exception("Successful insertion should return a lesson!"));

        assertTrue("Inserted lesson should have come from the original", db.originCheck(insertedLesson, lesson, true));
        assertInDatabase(insertedLesson);
    }

    @Test
    public void creatingLessonWithoutLength_shouldReturn400() throws Exception {
        SuppliedLesson lesson = SuppliedLesson.builder()
                .withGroupId(OLD_GROUP_ID)
                .withStartTime(Fake.time(NEW_LESSON_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isBadRequest());
    }

    @Test
    public void creatingLessonWithNonExistentGroupId_shouldReturn404() throws Exception {
        SuppliedLesson lesson = Fakes.suppliedLesson(NEW_LESSON_ID, NEW_GROUP_ID);

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isNotFound());
    }

    @Test
    public void creatingLessonWhichOverlaps_shouldReturn409() throws Exception {
        SuppliedLesson lesson = SuppliedLesson.builder()
                .withGroupId(OLD_GROUP_ID)
                .withStartTime(Fake.time(OLD_LESSON_ID))
                .withLength(Fake.duration(NEW_LESSON_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isConflict());
    }

    @Test
    public void updatingLessonWithId_shouldUpdateLesson() throws Exception {
        SuppliedLesson lesson = Fakes.suppliedLesson(NEW_LESSON_ID, OLD_GROUP_ID);

        mvc.performPost(DEFAULT_PATH + OLD_LESSON_ID, lesson, OK_NO_BODY);

        assertInDatabase(lesson, OLD_LESSON_ID);
    }

    @Test
    public void updatingLessonLengthOnly_shouldUpdateLesson() throws Exception {
        SuppliedLesson partialLesson = SuppliedLesson.builder()
                .withLength(Fake.duration(NEW_LESSON_ID))
                .build();

        mvc.performPost(DEFAULT_PATH + OLD_LESSON_ID, partialLesson, OK_NO_BODY);

        FetchedLesson beforeUpdate = Fakes.fetchedLesson(OLD_LESSON_ID);
        FetchedLesson afterUpdate = FetchedLesson.stepBuilder()
                .withId(beforeUpdate.getId())
                .withGroupId(beforeUpdate.getGroupId())
                .withTeacherId(beforeUpdate.getTeacherId())
                .withStartTime(beforeUpdate.getStartTime())
                .withEndTime(Instant.ofEpochMilli(beforeUpdate.getStartTime())
                        .plus(partialLesson.getLength(), MINUTES)
                        .toEpochMilli())
                .withLength(partialLesson.getLength())
                .withComment(beforeUpdate.getComment())
                .withCreatedAt(beforeUpdate.getCreatedAt())
                .withUpdatedAt(beforeUpdate.getUpdatedAt())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingLessonWithNonExistentId_shouldReturn404() throws Exception {
        SuppliedLesson lesson = Fakes.suppliedLesson(NEW_LESSON_ID, OLD_GROUP_ID);

        mvc.performPost(DEFAULT_PATH + NEW_LESSON_ID, lesson, BAD, status().isNotFound());

        assertNotInDatabase(NEW_LESSON_ID);
    }

    @Test
    public void updatingLessonWithNonExistentGroupId_shouldReturn404() throws Exception {
        SuppliedLesson lesson = Fakes.suppliedLesson(NEW_LESSON_ID, NEW_GROUP_ID);

        mvc.performPost(DEFAULT_PATH + OLD_LESSON_ID, lesson, BAD, status().isNotFound());

        assertInDatabase(Fakes.fetchedLesson(OLD_LESSON_ID));
    }

    @Test
    public void updatingLessonWhichOverlaps_shouldReturn409() throws Exception {
        FetchedLesson lessonToBeOverlapped = db.insertSuppliedLesson(Fakes.suppliedLesson(NEW_LESSON_ID, OLD_GROUP_ID), NEW_LESSON_ID);

        SuppliedLesson lesson = SuppliedLesson.builder()
                .withGroupId(OLD_GROUP_ID)
                .withStartTime(lessonToBeOverlapped.getStartTime())
                .withLength(lessonToBeOverlapped.getLength())
                .build();

        mvc.performPost(DEFAULT_PATH + OLD_LESSON_ID, lesson, BAD, status().isConflict());

        assertInDatabase(lessonToBeOverlapped);
        assertInDatabase(Fakes.fetchedLesson(OLD_LESSON_ID));
    }

    @Test
    public void deletingLessonById_shouldDeleteLesson() throws Exception {
        FetchedLesson lesson = db.insertSuppliedLesson(Fakes.suppliedLesson(NEW_LESSON_ID, OLD_GROUP_ID), NEW_LESSON_ID);

        mvc.performDelete(DEFAULT_PATH + lesson.getId(), OK_NO_BODY);

        assertNotInDatabase(lesson);
    }

    @Test
    public void deletingLessonByStillUsedId_shouldReturn400() throws Exception {
        mvc.performDelete(DEFAULT_PATH + OLD_LESSON_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_LESSON_ID);
    }

    @Test
    public void deletingLessonByNonExistentId_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + NEW_LESSON_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_LESSON_ID);
    }

    @Test
    public void readingLessonById_shouldReturnLesson() throws Exception {
        FetchedLesson lesson = mvc.performGet(DEFAULT_PATH + OLD_LESSON_ID, OK)
                .map(Unchecked.function(this::readLesson))
                .orElseThrow(() -> new Exception("Successful read should return a lesson!"));

        assertInDatabase(lesson);
    }

    @Test
    public void readingLessonByNonExistentId_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + NEW_LESSON_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingAllLessons_shouldReturnLessons() throws Exception {
        List<FetchedLesson> lessons = mvc.performGet(DEFAULT_PATH + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForTeacher_shouldReturnLessons() throws Exception {
        List<FetchedLesson> lessons = mvc.performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForNonExistentTeacher_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID + DEFAULT_PARAMS, BAD, status().isNotFound());
    }

    @Test
    public void readingLessonsForCustomer_shouldReturnLessons() throws Exception {
        List<FetchedLesson> lessons = mvc.performGet(DEFAULT_PATH + "customer/" + OLD_CUSTOMER_ID + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForNonExistentCustomer_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "customer/" + NEW_CUSTOMER_ID + DEFAULT_PARAMS, BAD, status().isNotFound());
    }

    @Test
    public void readingLessonsForGroup_shouldReturnLessons() throws Exception {
        List<FetchedLesson> lessons = mvc.performGet(DEFAULT_PATH + "group/" + OLD_GROUP_ID + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForNonExistentGroup_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "group/" + NEW_GROUP_ID + DEFAULT_PARAMS, BAD, status().isNotFound());
    }

    @Test
    public void readingLessonsForStudent_shouldReturnLessons() throws Exception {
        List<FetchedLesson> lessons = mvc.performGet(DEFAULT_PATH + "student/" + OLD_STUDENT_ID + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForNonExistentStudent_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "student/" + NEW_STUDENT_ID + DEFAULT_PARAMS, BAD, status().isNotFound());
    }

    // PRIVATE

    private FetchedLesson readLesson(MvcResult result) throws IOException {
        return MVC.from(result).to(FetchedLesson.class);
    }

    private List<FetchedLesson> readLessons(MvcResult result) throws IOException {
        return MVC.from(result).to(LIST_OF_LESSONS);
    }

    private void assertNotInDatabase(FetchedLesson lesson) {
        assertNotInDatabase(DB::readFetchedLesson, lesson.getId());
    }

    private void assertNotInDatabase(long lessonId) {
        assertNotInDatabase(DB::readFetchedLesson, lessonId);
    }

    private void assertInDatabase(FetchedLesson lesson) {
        assertInDatabase(DB::readFetchedLesson, FetchedLesson::getId, lesson);
    }

    private void assertInDatabase(long lessonId) {
        assertInDatabase(DB::readFetchedLesson, lessonId);
    }

    private void assertInDatabase(SuppliedLesson lesson, long lessonId) {
        assertInDatabase(DB::readSuppliedLesson, any -> lessonId, lesson);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v3/lesson/";
    private static final String DEFAULT_PARAMS = "?start=" + 0 + "&end=" + Long.MAX_VALUE;

    private static final TypeReference<List<FetchedLesson>> LIST_OF_LESSONS = new TypeReference<List<FetchedLesson>>() {};

}
