package com.superum.api.teacher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.env.IntegrationTestEnvironment;
import com.superum.helper.DB;
import com.superum.helper.Fake;
import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.superum.helper.ResultVariation.*;
import static com.superum.utils.MockMvcUtils.fromResponse;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ValidTeacherControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingTeacherWithoutId_shouldCreateNewTeacher() throws Exception {
        FullTeacherDTO teacher = Fake.teacher(NEW_TEACHER_ID).withoutId();

        FullTeacherDTO insertedTeacher = performPut(DEFAULT_PATH, teacher, OK)
                .map(Unchecked.function(this::readTeacher))
                .orElseThrow(() -> new Exception("Successful insertion should return a teacher!"));

        assertEquals("Inserted teacher should be equal to the original (except id)",
                teacher.withId(insertedTeacher.getId()), insertedTeacher);

        assertInDatabase(insertedTeacher);
    }

    @Test
    public void insertingTeacherWithId_shouldReturn400() throws Exception {
        FullTeacherDTO teacher = Fake.teacher(NEW_TEACHER_ID);

        performPut(DEFAULT_PATH, teacher, BAD, status().isBadRequest());

        assertNotInDatabase(teacher);
    }

    @Test
    public void insertingTeacherWithoutName_shouldReturn400() throws Exception {
        FullTeacherDTO teacher = FullTeacherDTO.builder()
                .paymentDay(Fake.day(NEW_TEACHER_ID))
                .hourlyWage(Fake.wage(NEW_TEACHER_ID))
                .academicWage(Fake.wage(NEW_TEACHER_ID))
                .surname(Fake.surname(NEW_TEACHER_ID))
                .phone(Fake.phone(NEW_TEACHER_ID))
                .city(Fake.city(NEW_TEACHER_ID))
                .email(Fake.email(NEW_TEACHER_ID))
                .languages(Fake.language(NEW_TEACHER_ID))
                .build();

        performPut(DEFAULT_PATH, teacher, BAD, status().isBadRequest());
    }

    @Test
    public void insertingTeacherWithDuplicateEmail_shouldReturn409() throws Exception {
        FullTeacherDTO teacher = FullTeacherDTO.stepBuilder()
                .paymentDay(Fake.day(NEW_TEACHER_ID))
                .hourlyWage(Fake.wage(NEW_TEACHER_ID))
                .academicWage(Fake.wage(NEW_TEACHER_ID))
                .name(Fake.name(NEW_TEACHER_ID))
                .surname(Fake.surname(NEW_TEACHER_ID))
                .phone(Fake.phone(NEW_TEACHER_ID))
                .city(Fake.city(NEW_TEACHER_ID))
                .email(Fake.email(OLD_TEACHER_ID))
                .languages(Fake.language(NEW_TEACHER_ID))
                .build();

        performPut(DEFAULT_PATH, teacher, BAD, status().isConflict());
    }

    @Test
    public void updatingTeacherWithId_shouldUpdateTeacher() throws Exception {
        FullTeacherDTO teacher = Fake.teacher(NEW_TEACHER_ID).withId(OLD_TEACHER_ID);

        performPost(DEFAULT_PATH, teacher, OK_NO_BODY);

        assertInDatabase(teacher);
    }

    @Test
    public void updatingTeacherWithIdAndNameOnly_shouldUpdateTeacher() throws Exception {
        FullTeacherDTO partialTeacher = FullTeacherDTO.builder()
                .id(OLD_TEACHER_ID)
                .name(Fake.name(NEW_TEACHER_ID))
                .build();

        performPost(DEFAULT_PATH, partialTeacher, OK_NO_BODY);

        FullTeacherDTO beforeUpdate = Fake.teacher(OLD_TEACHER_ID);
        FullTeacherDTO afterUpdate = FullTeacherDTO.stepBuilder()
                .paymentDay(beforeUpdate.getPaymentDay())
                .hourlyWage(beforeUpdate.getHourlyWage())
                .academicWage(beforeUpdate.getAcademicWage())
                .name(partialTeacher.getName())
                .surname(beforeUpdate.getSurname())
                .phone(beforeUpdate.getPhone())
                .city(beforeUpdate.getCity())
                .email(beforeUpdate.getEmail())
                .languages(beforeUpdate.getLanguages())
                .id(partialTeacher.getId())
                .picture(beforeUpdate.getPicture())
                .document(beforeUpdate.getDocument())
                .comment(beforeUpdate.getComment())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingTeacherWithoutId_shouldReturn400() throws Exception {
        FullTeacherDTO teacher = Fake.teacher(NEW_TEACHER_ID).withoutId();

        performPost(DEFAULT_PATH, teacher, BAD, status().isBadRequest());
    }

    @Test
    public void updatingTeacherWithOnlyId_shouldReturn400() throws Exception {
        FullTeacherDTO teacher = FullTeacherDTO.builder()
                .id(OLD_TEACHER_ID)
                .build();

        performPost(DEFAULT_PATH, teacher, BAD, status().isBadRequest());

        assertInDatabase(Fake.teacher(OLD_TEACHER_ID));
    }

    @Test
    public void updatingTeacherWithNonExistentId_shouldReturn404() throws Exception {
        FullTeacherDTO teacher = Fake.teacher(NEW_TEACHER_ID);

        performPost(DEFAULT_PATH, teacher, BAD, status().isNotFound());

        assertNotInDatabase(teacher);
    }

    @Test
    public void updatingTeacherWithDuplicateEmail_shouldReturn409() throws Exception {
        FullTeacherDTO teacher = FullTeacherDTO.stepBuilder()
                .paymentDay(Fake.day(NEW_TEACHER_ID))
                .hourlyWage(Fake.wage(NEW_TEACHER_ID))
                .academicWage(Fake.wage(NEW_TEACHER_ID))
                .name(Fake.name(NEW_TEACHER_ID))
                .surname(Fake.surname(NEW_TEACHER_ID))
                .phone(Fake.phone(NEW_TEACHER_ID))
                .city(Fake.city(NEW_TEACHER_ID))
                .email(Fake.email(OLD_TEACHER_ID))
                .languages(Fake.language(NEW_TEACHER_ID))
                .id(EXTRA_TEACHER_ID)
                .build();

        performPost(DEFAULT_PATH, teacher, BAD, status().isConflict());

        assertInDatabase(Fake.teacher(EXTRA_TEACHER_ID));
    }

    @Test
    public void deletingTeacherById_shouldDeleteTeacher() throws Exception {
        FullTeacherDTO teacher = db.insertFullTeacher(Fake.teacher(NEW_TEACHER_ID));
        db.insertValidAccount(teacher);

        performDelete(DEFAULT_PATH + teacher.getId(), OK_NO_BODY);

        assertNotInDatabase(teacher);
    }

    @Test
    public void deletingTeacherByStillUsedId_shouldReturn400() throws Exception {
        performDelete(DEFAULT_PATH + OLD_TEACHER_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_TEACHER_ID);
    }

    @Test
    public void deletingTeacherByNonExistentId_shouldReturn404() throws Exception {
        performDelete(DEFAULT_PATH + NEW_TEACHER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_TEACHER_ID);
    }

    @Test
    public void readingTeacherById_shouldReturnTeacher() throws Exception {
        FullTeacherDTO teacher = performGet(DEFAULT_PATH + OLD_TEACHER_ID, OK)
                .map(Unchecked.function(this::readTeacher))
                .orElseThrow(() -> new Exception("Successful read should return a teacher!"));

        assertInDatabase(teacher);
    }

    @Test
    public void readingTeacherByNonExistentId_shouldReturn404() throws Exception {
        performGet(DEFAULT_PATH + NEW_TEACHER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_TEACHER_ID);
    }

    @Test
    public void readingAllTeachers_shouldReturnTeachers() throws Exception {
        List<FullTeacherDTO> teachers = performGet(DEFAULT_PATH, OK)
                .map(Unchecked.function(this::readTeachers))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, teachers.size());
        teachers.forEach(this::assertInDatabase);
    }

    @Test
    public void countingAllTeachers_shouldReturn2() throws Exception {
        int count = performGet(DEFAULT_PATH + "count", OK)
                .map(Unchecked.function(this::readCount))
                .orElseThrow(() -> new Exception("Successful read should return an integer!"));

        assertEquals("When counting all, 2 should be returned", 2, count);
    }

    // PRIVATE

    private FullTeacherDTO readTeacher(MvcResult result) throws IOException {
        return fromResponse(result, FullTeacherDTO.class);
    }

    private List<FullTeacherDTO> readTeachers(MvcResult result) throws IOException {
        return fromResponse(result, LIST_OF_TEACHERS);
    }

    private void assertNotInDatabase(FullTeacherDTO teacher) {
        assertNotInDatabase(DB::readFullTeacher, teacher.getId());
    }

    private void assertNotInDatabase(int teacherId) {
        assertNotInDatabase(DB::readFullTeacher, teacherId);
    }

    private void assertInDatabase(FullTeacherDTO teacher) {
        assertInDatabase(DB::readFullTeacher, FullTeacherDTO::getId, teacher);
    }

    private void assertInDatabase(int teacherId) {
        assertInDatabase(DB::readFullTeacher, teacherId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/teacher/";
    private static final int EXTRA_TEACHER_ID = 2;

    private static final TypeReference<List<FullTeacherDTO>> LIST_OF_TEACHERS = new TypeReference<List<FullTeacherDTO>>() {};

}
