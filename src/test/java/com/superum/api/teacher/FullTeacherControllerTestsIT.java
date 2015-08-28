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
public class FullTeacherControllerTestsIT extends IntegrationTestEnvironment {

    @Test
    public void insertingTeacherWithoutId_shouldCreateNewTeacher() throws Exception {
        FullTeacherDTO teacher = makeFakeFullTeacher(TEACHER_SEED).withoutId();

        MvcResult result = mockMvc.perform(put("/timestar/api/v2/teacher")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(teacher)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacherDTO returnedTeacher = fromResponse(result, FullTeacherDTO.class);
        int teacherId = returnedTeacher.getId();
        teacher = teacher.withId(teacherId);

        assertEquals("The returned teacher should be equal to original teacher, except for id field; ", returnedTeacher, teacher);

        FullTeacherDTO teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the returned teacher; ", teacherFromDB, returnedTeacher);
    }

    @Test
    public void readingTeacherWithValidId_shouldReturnATeacher() throws Exception {
        FullTeacherDTO insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher/{teacherId}", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTeacherDTO returnedTeacher = fromResponse(result, FullTeacherDTO.class);

        assertEquals("The read teacher should be equal to original teacher; ", returnedTeacher, insertedTeacher);

        FullTeacherDTO teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the returned teacher; ", teacherFromDB, returnedTeacher);
    }

    @Test
    public void updatingTeacherWithValidData_shouldReturnOldTeacher() throws Exception {
        FullTeacherDTO insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        FullTeacherDTO updatedTeacher = makeFakeFullTeacher(TEACHER_SEED + 1).withId(teacherId);

        mockMvc.perform(post("/timestar/api/v2/teacher")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(updatedTeacher)))
                .andDo(print())
                .andExpect(status().isOk());

        FullTeacherDTO teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertEquals("The teacher in the database should be equal to the updated teacher; ", teacherFromDB, updatedTeacher);
    }

    @Test
    public void updatingPartialTeacherWithValidData_shouldReturnOldTeacher() throws Exception {
        FullTeacherDTO insertedTeacher = databaseHelper.insertFullTeacherIntoDb(makeFakeFullTeacher(TEACHER_SEED).withoutId());
        int teacherId = insertedTeacher.getId();

        FullTeacherDTO partialUpdatedTeacher = makeFakeFullTeacher(teacherId, null, null, null,
                fakeName(TEACHER_SEED + 1), null, fakePhone(TEACHER_SEED + 1),
                null, null, null, null, null, (List<String>) null);

        mockMvc.perform(post("/timestar/api/v2/teacher")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(partialUpdatedTeacher)))
                .andDo(print())
                .andExpect(status().isOk());

        FullTeacherDTO teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);
        FullTeacherDTO updatedTeacher = makeFakeFullTeacher(teacherId, insertedTeacher.getPaymentDay(),
                insertedTeacher.getHourlyWage(), insertedTeacher.getAcademicWage(),
                partialUpdatedTeacher.getName(), insertedTeacher.getSurname(), partialUpdatedTeacher.getPhone(),
                insertedTeacher.getCity(), insertedTeacher.getEmail(), insertedTeacher.getPicture(),
                insertedTeacher.getDocument(), insertedTeacher.getComment(), insertedTeacher.getLanguages());

        assertEquals("The teacher in the database should be equal to the updated teacher; ", teacherFromDB, updatedTeacher);
    }

    @Test
    public void updatingTeacherWithInvalidId_shouldReturnBadRequest() throws Exception {
        FullTeacherDTO validTeacher = makeFakeFullTeacher(TEACHER_SEED);

        String json = convertObjectToString(validTeacher);
        String invalidJson = replace(json, "id", -1);

        mockMvc.perform(post("/timestar/api/v2/teacher")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletingTeacherWithValidId_shouldReturnDeletedTeacher() throws Exception {
        FullTeacherDTO fullTeacher = makeFakeFullTeacher(TEACHER_SEED).withoutId();
        FullTeacherDTO insertedTeacher = databaseHelper.insertFullTeacherIntoDb(fullTeacher);
        databaseHelper.insertAccountIntoDb(fullTeacher);
        int teacherId = insertedTeacher.getId();

        mockMvc.perform(delete("/timestar/api/v2/teacher/{teacherId}", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk());

        FullTeacherDTO teacherFromDB = databaseHelper.readFullTeacherFromDb(teacherId);

        assertNull("The teacher from the database should be equal null, since it was deleted; ", teacherFromDB);
    }

    @Test
    public void readingAllTeachers_shouldReturnListOfTeachers() throws Exception {
        List<FullTeacherDTO> allTeachers = makeSomeFakes(2, FakeUtils::makeFakeFullTeacher).stream()
                .map(FullTeacherDTO::withoutId)
                .map(databaseHelper::insertFullTeacherIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<FullTeacherDTO> returnedTeachers = fromResponse(result, LIST_OF_TEACHERS);

        List<FullTeacherDTO> teachersFromDb = allTeachers.stream()
                .mapToInt(FullTeacherDTO::getId)
                .mapToObj(databaseHelper::readFullTeacherFromDb)
                .collect(Collectors.toList());

        assertEquals("The read teachers should be equal to original teachers; ", returnedTeachers, allTeachers);
        assertEquals("The teachers in the database should be equal to the read teachers; ", teachersFromDb, returnedTeachers);
    }

    @Test
    public void countingAllTeachers_shouldReturnCount() throws Exception {
        List<FullTeacherDTO> allTeachers = makeSomeFakes(2, FakeUtils::makeFakeFullTeacher).stream()
                .map(FullTeacherDTO::withoutId)
                .map(databaseHelper::insertFullTeacherIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/teacher/count")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount = fromResponse(result, Integer.class);

        assertEquals("The amount of read teachers should be equal to original amount of teachers; ", returnedCount, allTeachers.size());

        long amountOfTeachersInDb = allTeachers.stream()
                .mapToInt(FullTeacherDTO::getId)
                .mapToObj(databaseHelper::readFullTeacherFromDb)
                .filter(teacher -> teacher != null)
                .count();

        assertEquals("The amount of teachers in the database should be equal to the amount of read teachers; ", amountOfTeachersInDb, returnedCount);
    }

    // PRIVATE

    private static final TypeReference<List<FullTeacherDTO>> LIST_OF_TEACHERS = new TypeReference<List<FullTeacherDTO>>() {};
    private static final int TEACHER_SEED = 1;

}
