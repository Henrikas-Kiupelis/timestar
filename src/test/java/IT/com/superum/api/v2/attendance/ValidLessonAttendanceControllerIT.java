package IT.com.superum.api.v2.attendance;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.superum.api.v2.attendance.ValidLessonAttendanceDTO;
import com.superum.helper.Fakes;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static eu.goodlike.libraries.mockmvc.HttpResult.BAD;
import static eu.goodlike.libraries.mockmvc.HttpResult.OK_NO_BODY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ValidLessonAttendanceControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingAttendance_shouldCreateNewAttendance() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(ADDITIONAL_LESSON_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, attendance, OK_NO_BODY);

        assertInDatabase(attendance);
    }

    @Test
    public void insertingAttendanceWithStudentsFromWrongGroup_shouldReturn400() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(ADDITIONAL_LESSON_ID, EXTRA_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, attendance, BAD, status().isBadRequest());

        assertNotInDatabase(attendance);
    }

    @Test
    public void insertingAttendanceForNonExistentLesson_shouldReturn404() throws Exception {
        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(NEW_LESSON_ID, OLD_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, attendance, BAD, status().isNotFound());

        assertNotInDatabase(attendance);
    }

    @Test
    public void insertingAttendanceForNonExistentStudent_shouldReturn404() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(ADDITIONAL_LESSON_ID, NEW_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, attendance, BAD, status().isNotFound());

        assertNotInDatabase(attendance);
    }

    @Test
    public void insertingAttendanceWhenAttendanceAlreadyExists_shouldReturn409() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(OLD_LESSON_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, attendance, BAD, status().isConflict());

        assertInDatabase(Fakes.lessonAttendance(OLD_LESSON_ID));
    }

    @Test
    public void updatingAttendance_shouldUpdateAttendance() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(OLD_LESSON_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, attendance, OK_NO_BODY);

        assertInDatabase(attendance);
    }

    @Test
    public void updatingAttendanceWithStudentsFromWrongGroup_shouldReturn400() throws Exception {
        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(OLD_LESSON_ID, EXTRA_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, attendance, BAD, status().isBadRequest());

        assertInDatabase(Fakes.lessonAttendance(OLD_LESSON_ID));
    }

    @Test
    public void updatingAttendanceForNonExistentLesson_shouldReturn404() throws Exception {
        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(NEW_LESSON_ID, OLD_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, attendance, BAD, status().isNotFound());

        assertNotInDatabase(attendance);
    }

    @Test
    public void updatingAttendanceForNonExistentStudent_shouldReturn404() throws Exception {
        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(OLD_LESSON_ID, NEW_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, attendance, BAD, status().isNotFound());

        assertInDatabase(Fakes.lessonAttendance(OLD_LESSON_ID));
    }

    @Test
    public void updatingAttendanceWhenAttendanceDoesNotExist_shouldReturn404() throws Exception {
        additionalSetup();

        ValidLessonAttendanceDTO attendance = Fakes.lessonAttendance(ADDITIONAL_LESSON_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, attendance, BAD, status().isNotFound());

        assertNotInDatabase(attendance);
    }

    @Test
    public void deletingAttendanceForLesson_shouldDeleteAttendance() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "lesson/" + OLD_LESSON_ID, OK_NO_BODY);

        assertNotInDatabaseForLessonId(OLD_LESSON_ID);
    }

    @Test
    public void deletingAttendanceForNonExistentLesson_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "lesson/" + NEW_LESSON_ID, BAD, status().isNotFound());

        assertNotInDatabaseForLessonId(NEW_LESSON_ID);
    }

    @Test
    public void deletingAttendanceForLessonWhenAttendanceDoesNotExist_shouldReturn404() throws Exception {
        additionalSetup();

        mvc.performDelete(DEFAULT_PATH + "lesson/" + ADDITIONAL_LESSON_ID, BAD, status().isNotFound());

        assertNotInDatabaseForLessonId(ADDITIONAL_LESSON_ID);
    }

    @Test
    public void deletingAttendanceForStudent_shouldDeleteAttendance() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "student/" + OLD_STUDENT_ID, OK_NO_BODY);

        assertNotInDatabaseForStudentId(OLD_STUDENT_ID);
    }

    @Test
    public void deletingAttendanceForNonExistentStudent_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "student/" + NEW_STUDENT_ID, BAD, status().isNotFound());

        assertNotInDatabaseForStudentId(NEW_STUDENT_ID);
    }

    @Test
    public void deletingAttendanceForStudentWhenAttendanceDoesNotExist_shouldReturn404() throws Exception {
        additionalSetup();

        mvc.performDelete(DEFAULT_PATH + "student/" + ADDITIONAL_STUDENT_ID, BAD, status().isNotFound());

        assertNotInDatabaseForStudentId(ADDITIONAL_STUDENT_ID);
    }

    // PRIVATE

    private void assertInDatabase(ValidLessonAttendanceDTO attendance) {
        assertInDatabase(DB::readValidLessonAttendance, ValidLessonAttendanceDTO::getLessonId, attendance);
    }

    private void assertNotInDatabase(ValidLessonAttendanceDTO attendance) {
        assertNotInDatabase(DB::readValidLessonAttendance, attendance.getLessonId());
    }

    private void assertNotInDatabaseForLessonId(long lessonId) {
        assertNotInDatabase(DB::readValidLessonAttendance, lessonId);
    }

    private void assertNotInDatabaseForStudentId(int studentId) {
        assertNotInDatabaseSecondary(DB::existsLessonAttendanceForStudentId, studentId);
    }

    private void additionalSetup() {
        db.insertValidLesson(Fakes.lesson(ADDITIONAL_LESSON_ID, OLD_GROUP_ID, OLD_TEACHER_ID));
        db.insertValidStudent(Fakes.student(ADDITIONAL_STUDENT_ID, null));
        db.insertValidGrouping(Fakes.grouping(OLD_GROUP_ID, ADDITIONAL_STUDENT_ID));
    }

    private static final long ADDITIONAL_LESSON_ID = 3;
    private static final int ADDITIONAL_STUDENT_ID = 3;

    private static final String DEFAULT_PATH = "/timestar/api/v2/lesson/attendance/";

}
