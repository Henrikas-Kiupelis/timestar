package com.superum.api.teacher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.utils.FakeUtils;
import env.IntegrationTestEnvironment;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.superum.utils.FakeFieldUtils.fakeName;
import static com.superum.utils.FakeFieldUtils.fakePhone;
import static com.superum.utils.FakeUtils.makeFakeFullTeacher;
import static com.superum.utils.FakeUtils.makeSomeFakes;
import static com.superum.utils.JsonUtils.*;
import static com.superum.utils.MockMvcUtils.fromResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FullTeacherControllerTests extends IntegrationTestEnvironment {

    @Test
    public void insertingTeacherWithoutId_shouldCreateNewTeacher() throws Exception {
        FullTeacher teacher = makeFakeFullTeacher(TEACHER_SEED).withoutId();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/teacher/create")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(teacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);
        int teacherId = returnedTeacher.getId();
        teacher = teacher.withId(teacherId);

        assertEquals("The returned teacher should be equal to original teacher, except for id field; ", returnedTeacher, teacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the returned teacher; ", teacherFromDB, returnedTeacher);
    }

    @Test
    public void readingTeacherWithValidId_shouldReturnATeacher() throws Exception {
        FullTeacher insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher/{teacherId}", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the returned teacher; ", teacherFromDB, returnedTeacher);
    }

    @Test
    public void updatingTeacherWithValidData_shouldReturnOldTeacher() throws Exception {
        FullTeacher insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        FullTeacher updatedTeacher = makeFakeFullTeacher(TEACHER_SEED + 1).withId(teacherId);

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/teacher/update")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(updatedTeacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the updated teacher; ", teacherFromDB, updatedTeacher);
    }

    @Test
    public void updatingPartialTeacherWithValidData_shouldReturnOldTeacher() throws Exception {
        FullTeacher insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        FullTeacher partialUpdatedTeacher = makeFakeFullTeacher(teacherId, 0, null, null,
                fakeName(TEACHER_SEED + 1), null, fakePhone(TEACHER_SEED + 1),
                null, null, null, null, null, (List<String>)null);

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/teacher/update")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(partialUpdatedTeacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);
        FullTeacher updatedTeacher = makeFakeFullTeacher(teacherId, insertedTeacher.getPaymentDay(),
                insertedTeacher.getHourlyWage(), insertedTeacher.getAcademicWage(),
                partialUpdatedTeacher.getName(), insertedTeacher.getSurname(), partialUpdatedTeacher.getPhone(),
                insertedTeacher.getCity(), insertedTeacher.getEmail(), insertedTeacher.getPicture(),
                insertedTeacher.getDocument(), insertedTeacher.getComment(), insertedTeacher.getLanguages());

        assertEquals("The teacher in the database should be equal to the updated teacher; ", teacherFromDB, updatedTeacher);
    }

    @Test
    public void updatingTeacherWithInvalidId_shouldReturnBadRequest() throws Exception {
        FullTeacher validTeacher = makeFakeFullTeacher(TEACHER_SEED);

        String json = convertObjectToString(validTeacher);
        String invalidJson = replace(json, "id", -1);

        mockMvc.perform(post("/timestar/api/v2/teacher/update")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletingTeacherWithValidId_shouldReturnDeletedTeacher() throws Exception {
        FullTeacher insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        MvcResult result = mockMvc.perform(delete("/timestar/api/v2/teacher/delete/{teacherId}", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertNull("The teacher from the database should be equal null, since it was deleted; ", teacherFromDB);
    }

    @Test
    public void readingAllTeachers_shouldReturnListOfTeachers() throws Exception {
        List<FullTeacher> allTeachers = makeSomeFakes(2, FakeUtils::makeFakeFullTeacher).stream()
                .map(FullTeacher::withoutId)
                .map(databaseHelper::insertFullTeacherIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher/all")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<FullTeacher> returnedTeachers = fromResponse(result, LIST_OF_TEACHERS);

        assertEquals("The read teachers should be equal to original teachers; ", returnedTeachers, allTeachers);

        List<FullTeacher> teachersFromDb = allTeachers.stream()
                .mapToInt(FullTeacher::getId)
                .mapToObj(databaseHelper::readFullTeacherFromDb)
                .collect(Collectors.toList());

        assertEquals("The teachers in the database should be equal to the read teachers; ", teachersFromDb, returnedTeachers);
    }

    @Test
    public void countingAllTeachers_shouldReturnCount() throws Exception {
        List<FullTeacher> allTeachers = makeSomeFakes(2, FakeUtils::makeFakeFullTeacher).stream()
                .map(FullTeacher::withoutId)
                .map(databaseHelper::insertFullTeacherIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher/all/count")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount = fromResponse(result, Integer.class);

        assertEquals("The amount of read teachers should be equal to original amount of teachers; ", returnedCount, allTeachers.size());

        long amountOfTeachersInDb = allTeachers.stream()
                .mapToInt(FullTeacher::getId)
                .mapToObj(databaseHelper::readFullTeacherFromDb)
                .filter(teacher -> teacher != null)
                .count();

        assertEquals("The amount of teachers in the database should be equal to the amount of read teachers; ", amountOfTeachersInDb, returnedCount);
    }

    @Test
    public void doesTeacherExist_shouldReturnExistingTeacher() throws Exception {
        FullTeacher insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/teacher/exists")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(insertedTeacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacher returnedTeacher = fromResponse(result, FullTeacher.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacher teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the returned teacher; ", teacherFromDB, returnedTeacher);
    }

    // PRIVATE

    private static final TypeReference<List<FullTeacher>> LIST_OF_TEACHERS = new TypeReference<List<FullTeacher>>() {};
    private static final int TEACHER_SEED = 1;

}
