package IT.com.superum.api.v2;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.v2.lesson.ValidLessonDTO;
import com.superum.helper.Fakes;
import eu.goodlike.libraries.spring.mockmvc.MVC;
import eu.goodlike.test.Fake;
import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static eu.goodlike.libraries.spring.mockmvc.HttpResult.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ValidLessonControllerIT extends IntegrationTestEnvironment {

    @Test
    public void creatingLessonWithoutId_shouldCreateNewLesson() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID).withoutId();

        ValidLessonDTO insertedLesson = mvc.performPost(DEFAULT_PATH, lesson, OK)
                .map(Unchecked.function(this::readLesson))
                .orElseThrow(() -> new Exception("Successful insertion should return a lesson!"));

        assertEquals("Inserted lesson should be equal to the original (except id)",
                lesson.withId(insertedLesson.getId()), insertedLesson);

        assertInDatabase(insertedLesson);
    }

    @Test
    public void creatingLessonWithId_shouldReturn400() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID);

        mvc.performPost(DEFAULT_PATH, lesson, BAD, status().isBadRequest());

        assertNotInDatabase(lesson);
    }

    @Test
    public void creatingLessonWithoutLength_shouldReturn400() throws Exception {
        ValidLessonDTO lesson = ValidLessonDTO.builder()
                .groupIdWithTeacher(OLD_GROUP_ID, OLD_TEACHER_ID)
                .startTime(Fake.time(NEW_LESSON_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, lesson, BAD, status().isBadRequest());
    }

    @Test
    public void creatingLessonWithNonExistentGroupId_shouldReturn404() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, NEW_GROUP_ID, OLD_TEACHER_ID).withoutId();

        mvc.performPost(DEFAULT_PATH, lesson, BAD, status().isNotFound());
    }

    @Test
    public void creatingLessonWhichOverlaps_shouldReturn409() throws Exception {
        ValidLessonDTO lesson = ValidLessonDTO.stepBuilder()
                .groupIdWithTeacher(OLD_GROUP_ID, OLD_TEACHER_ID)
                .startTime(Fake.time(OLD_LESSON_ID))
                .length(Fake.duration(NEW_LESSON_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, lesson, BAD, status().isConflict());
    }

    @Test
    public void updatingLessonWithId_shouldUpdateLesson() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID).withId(OLD_LESSON_ID);

        mvc.performPut(DEFAULT_PATH, lesson, OK_NO_BODY);

        assertInDatabase(lesson);
    }

    @Test
    public void updatingLessonWithIdAndLengthOnly_shouldUpdateLesson() throws Exception {
        ValidLessonDTO partialLesson = ValidLessonDTO.builder()
                .id(OLD_LESSON_ID)
                .length(Fake.duration(NEW_LESSON_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, partialLesson, OK_NO_BODY);

        ValidLessonDTO beforeUpdate = Fakes.lesson(OLD_LESSON_ID);
        ValidLessonDTO afterUpdate = ValidLessonDTO.stepBuilder()
                .groupIdWithTeacher(beforeUpdate.getGroupId(), beforeUpdate.getTeacherId())
                .startTime(beforeUpdate.getStartTime())
                .length(partialLesson.getLength())
                .id(partialLesson.getId())
                .comment(beforeUpdate.getComment())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingLessonWithoutId_shouldReturn400() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID).withoutId();

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isBadRequest());
    }

    @Test
    public void updatingLessonWithOnlyId_shouldReturn400() throws Exception {
        ValidLessonDTO lesson = ValidLessonDTO.builder()
                .id(OLD_LESSON_ID)
                .build();

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isBadRequest());

        assertInDatabase(Fakes.lesson(OLD_LESSON_ID));
    }

    @Test
    public void updatingLessonWithNonExistentId_shouldReturn404() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID);

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isNotFound());

        assertNotInDatabase(lesson);
    }

    @Test
    public void updatingLessonWithNonExistentGroupId_shouldReturn404() throws Exception {
        ValidLessonDTO lesson = Fakes.lesson(NEW_LESSON_ID, NEW_GROUP_ID, OLD_TEACHER_ID).withId(OLD_LESSON_ID);

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isNotFound());

        assertInDatabase(Fakes.lesson(OLD_LESSON_ID));
    }

    @Test
    public void updatingLessonWhichOverlaps_shouldReturn409() throws Exception {
        ValidLessonDTO lessonToBeOverlapped = db.insertValidLesson(Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID));

        ValidLessonDTO lesson = ValidLessonDTO.stepBuilder()
                .groupIdWithTeacher(OLD_GROUP_ID, OLD_TEACHER_ID)
                .startTime(lessonToBeOverlapped.getStartTime())
                .length(lessonToBeOverlapped.getLength())
                .id(OLD_LESSON_ID)
                .build();

        mvc.performPut(DEFAULT_PATH, lesson, BAD, status().isConflict());

        assertInDatabase(lessonToBeOverlapped);
        assertInDatabase(Fakes.lesson(OLD_LESSON_ID));
    }

    @Test
    public void deletingLessonById_shouldDeleteLesson() throws Exception {
        ValidLessonDTO lesson = db.insertValidLesson(Fakes.lesson(NEW_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID));

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
        ValidLessonDTO lesson = mvc.performGet(DEFAULT_PATH + OLD_LESSON_ID, OK)
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
        List<ValidLessonDTO> lessons = mvc.performGet(DEFAULT_PATH + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readLessons))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, lessons.size());
        lessons.forEach(this::assertInDatabase);
    }

    @Test
    public void readingLessonsForTeacher_shouldReturnLessons() throws Exception {
        List<ValidLessonDTO> lessons = mvc.performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID + DEFAULT_PARAMS, OK)
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
        List<ValidLessonDTO> lessons = mvc.performGet(DEFAULT_PATH + "customer/" + OLD_CUSTOMER_ID + DEFAULT_PARAMS, OK)
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
        List<ValidLessonDTO> lessons = mvc.performGet(DEFAULT_PATH + "group/" + OLD_GROUP_ID + DEFAULT_PARAMS, OK)
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
        List<ValidLessonDTO> lessons = mvc.performGet(DEFAULT_PATH + "student/" + OLD_STUDENT_ID + DEFAULT_PARAMS, OK)
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

    private ValidLessonDTO readLesson(MvcResult result) throws IOException {
        return MVC.from(result).to(ValidLessonDTO.class);
    }

    private List<ValidLessonDTO> readLessons(MvcResult result) throws IOException {
        return MVC.from(result).to(LIST_OF_LESSONS);
    }

    private void assertNotInDatabase(ValidLessonDTO lesson) {
        assertNotInDatabase(DB::readValidLesson, lesson.getId());
    }

    private void assertNotInDatabase(long lessonId) {
        assertNotInDatabase(DB::readValidLesson, lessonId);
    }

    private void assertInDatabase(ValidLessonDTO lesson) {
        assertInDatabase(DB::readValidLesson, ValidLessonDTO::getId, lesson);
    }

    private void assertInDatabase(long lessonId) {
        assertInDatabase(DB::readValidLesson, lessonId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/lesson/";
    private static final String DEFAULT_PARAMS = "?start=" + 0 + "&end=" + Long.MAX_VALUE;

    private static final TypeReference<List<ValidLessonDTO>> LIST_OF_LESSONS = new TypeReference<List<ValidLessonDTO>>() {};

}
