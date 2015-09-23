package IT.com.superum.api.v2;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.v2.student.ValidStudentDTO;
import com.superum.helper.Fakes;
import eu.goodlike.libraries.mockmvc.MVC;
import eu.goodlike.test.Fake;
import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import static eu.goodlike.libraries.mockmvc.HttpResult.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ValidStudentControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingStudentWithoutId_shouldCreateNewStudent() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID).withoutId();

        ValidStudentDTO insertedStudent = mvc.performPut(DEFAULT_PATH, student, OK)
                .map(Unchecked.function(this::readStudent))
                .orElseThrow(() -> new Exception("Successful insertion should return a student!"));

        assertEquals("Inserted student should be equal to the original (except id and code)",
                student.withId(insertedStudent.getId()).withCode(insertedStudent.getCode()), insertedStudent);

        assertInDatabase(insertedStudent);
    }

    @Test
    public void insertingStudentWithId_shouldReturn400() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID);

        mvc.performPut(DEFAULT_PATH, student, BAD, status().isBadRequest());

        assertNotInDatabase(student);
    }

    @Test
    public void insertingStudentWithoutName_shouldReturn400() throws Exception {
        ValidStudentDTO namelessStudent = ValidStudentDTO.builder()
                .customerId(OLD_CUSTOMER_ID)
                .email(Fake.email(NEW_STUDENT_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, namelessStudent, BAD, status().isBadRequest());
    }

    @Test
    public void insertingStudentWithoutEmail_shouldReturn400() throws Exception {
        ValidStudentDTO noEmailStudent = ValidStudentDTO.builder()
                .customerId(OLD_CUSTOMER_ID)
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, noEmailStudent, BAD, status().isBadRequest());
    }

    @Test
    public void insertingStudentWithoutCustomerIdOrStartDate_shouldReturn400() throws Exception {
        ValidStudentDTO student = ValidStudentDTO.builder()
                .email(Fake.email(NEW_STUDENT_ID))
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, student, BAD, status().isBadRequest());
    }

    @Test
    public void insertingStudentWithBothCustomerIdAndStartDate_shouldReturn400() throws Exception {
        ValidStudentDTO student = ValidStudentDTO.builder()
                .customerId(OLD_CUSTOMER_ID)
                .startDate(Fakes.localDate(NEW_STUDENT_ID))
                .email(Fake.email(NEW_STUDENT_ID))
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, student, BAD, status().isBadRequest());
    }

    @Test
    public void insertingStudentWithNonExistentCustomerId_shouldReturn404() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, NEW_CUSTOMER_ID).withoutId();

        mvc.performPut(DEFAULT_PATH, student, BAD, status().isNotFound());
    }

    @Test
    public void updatingStudentWithId_shouldUpdateStudent() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID).withId(OLD_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, student, OK_NO_BODY);

        assertInDatabase(student, this::customEquals);
    }

    @Test
    public void updatingStudentWithIdAndNameOnly_shouldUpdateStudent() throws Exception {
        ValidStudentDTO partialStudent = ValidStudentDTO.builder()
                .id(OLD_STUDENT_ID)
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, partialStudent, OK_NO_BODY);

        ValidStudentDTO beforeUpdate = Fakes.student(OLD_STUDENT_ID);
        ValidStudentDTO afterUpdate = ValidStudentDTO.stepBuilder()
                .customerId(beforeUpdate.getCustomerId())
                .email(beforeUpdate.getEmail())
                .name(partialStudent.getName())
                .id(partialStudent.getId())
                .build();

        assertInDatabase(afterUpdate, this::customEquals);
    }

    @Test
    public void updatingStudentWithoutId_shouldReturn400() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID).withoutId();

        mvc.performPost(DEFAULT_PATH, student, BAD, status().isBadRequest());
    }

    @Test
    public void updatingStudentWithOnlyId_shouldReturn400() throws Exception {
        ValidStudentDTO student = ValidStudentDTO.builder()
                .id(OLD_STUDENT_ID)
                .build();

        mvc.performPost(DEFAULT_PATH, student, BAD, status().isBadRequest());

        assertInDatabase(Fakes.student(OLD_STUDENT_ID));
    }

    @Test
    public void updatingStudentWithBothCustomerIdAndStartDate_shouldReturn400() throws Exception {
        ValidStudentDTO student = ValidStudentDTO.builder()
                .id(OLD_STUDENT_ID)
                .customerId(OLD_CUSTOMER_ID)
                .startDate(Fakes.localDate(NEW_STUDENT_ID))
                .email(Fake.email(NEW_STUDENT_ID))
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, student, BAD, status().isBadRequest());

        assertInDatabase(Fakes.student(OLD_STUDENT_ID));
    }

    @Test
    public void updatingStudentWithNonExistentId_shouldReturn404() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID);

        mvc.performPost(DEFAULT_PATH, student, BAD, status().isNotFound());

        assertNotInDatabase(student);
    }

    @Test
    public void updatingStudentWithNonExistentCustomerId_shouldReturn404() throws Exception {
        ValidStudentDTO student = Fakes.student(NEW_STUDENT_ID, NEW_CUSTOMER_ID).withId(OLD_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, student, BAD, status().isNotFound());

        assertInDatabase(Fakes.student(OLD_STUDENT_ID));
    }

    @Test
    public void deletingStudentById_shouldDeleteStudent() throws Exception {
        ValidStudentDTO student = db.insertValidStudent(Fakes.student(NEW_STUDENT_ID, OLD_CUSTOMER_ID));

        mvc.performDelete(DEFAULT_PATH + student.getId(), OK_NO_BODY);

        assertNotInDatabase(student);
    }

    @Test
    public void deletingStudentByStillUsedId_shouldReturn400() throws Exception {
        mvc.performDelete(DEFAULT_PATH + OLD_STUDENT_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_STUDENT_ID);
    }

    @Test
    public void deletingStudentByNonExistentId_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + NEW_STUDENT_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_STUDENT_ID);
    }

    @Test
    public void readingStudentById_shouldReturnStudent() throws Exception {
        ValidStudentDTO readStudent = mvc.performGet(DEFAULT_PATH + OLD_STUDENT_ID, OK)
                .map(Unchecked.function(this::readStudent))
                .orElseThrow(() -> new Exception("Successful read should return a student!"));

        assertInDatabase(readStudent);
    }

    @Test
    public void readingStudentByNonExistentId_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + NEW_STUDENT_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingAllStudents_shouldReturnStudents() throws Exception {
        List<ValidStudentDTO> students = mvc.performGet(DEFAULT_PATH, OK)
                .map(Unchecked.function(this::readStudents))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, students.size());
        students.forEach(this::assertInDatabase);
    }

    @Test
    public void readingStudentsForGroup_shouldReturnStudents() throws Exception {
        List<ValidStudentDTO> students = mvc.performGet(DEFAULT_PATH + "group/" + OLD_GROUP_ID, OK)
                .map(Unchecked.function(this::readStudents))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, students.size());
        students.forEach(this::assertInDatabase);
    }

    @Test
    public void readingStudentsForNonExistentGroup_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "group/" + NEW_GROUP_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingStudentsForLesson_shouldReturnStudents() throws Exception {
        List<ValidStudentDTO> students = mvc.performGet(DEFAULT_PATH + "lesson/" + OLD_LESSON_ID, OK)
                .map(Unchecked.function(this::readStudents))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, students.size());
        students.forEach(this::assertInDatabase);
    }

    @Test
    public void readingStudentsForNonExistentLesson_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "lesson/" + NEW_LESSON_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingStudentsForCustomer_shouldReturnStudents() throws Exception {
        List<ValidStudentDTO> students = mvc.performGet(DEFAULT_PATH + "customer/" + OLD_CUSTOMER_ID, OK)
                .map(Unchecked.function(this::readStudents))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, students.size());
        students.forEach(this::assertInDatabase);
    }

    @Test
    public void readingStudentsForNonExistentCustomer_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "customer/" + NEW_CUSTOMER_ID, BAD, status().isNotFound());
    }

    // PRIVATE

    private ValidStudentDTO readStudent(MvcResult result) throws IOException {
        return MVC.from(result).to(ValidStudentDTO.class);
    }

    private List<ValidStudentDTO> readStudents(MvcResult result) throws IOException {
        return MVC.from(result).to(LIST_OF_STUDENTS);
    }

    private boolean customEquals(ValidStudentDTO t1, ValidStudentDTO t2) {
        return t1 == null
                ? t2 == null
                : Objects.equals(t1.getId(), t2.getId()) &&
                Objects.equals(t1.getCustomerId(), t2.getCustomerId()) &&
                Objects.equals(t1.getStartDate(), t2.getStartDate()) &&
                Objects.equals(t1.getEmail(), t2.getEmail()) &&
                Objects.equals(t1.getName(), t2.getName());
    }

    private void assertNotInDatabase(ValidStudentDTO student) {
        assertNotInDatabase(DB::readValidStudent, student.getId());
    }

    private void assertNotInDatabase(int studentId) {
        assertNotInDatabase(DB::readValidStudent, studentId);
    }

    private void assertInDatabase(ValidStudentDTO student) {
        assertInDatabase(DB::readValidStudent, ValidStudentDTO::getId, student);
    }

    private void assertInDatabase(ValidStudentDTO student, BiPredicate<ValidStudentDTO, ValidStudentDTO> customEqualityCheck) {
        assertInDatabase(DB::readValidStudent, ValidStudentDTO::getId, student, customEqualityCheck);
    }

    private void assertInDatabase(int studentId) {
        assertInDatabase(DB::readValidStudent, studentId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/student/";
    private static final TypeReference<List<ValidStudentDTO>> LIST_OF_STUDENTS = new TypeReference<List<ValidStudentDTO>>() {};

}
