package IT.com.superum.api.v2;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.superum.api.v2.grouping.ValidGroupingDTO;
import com.superum.helper.Fakes;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static eu.goodlike.libraries.spring.mockmvc.HttpResult.BAD;
import static eu.goodlike.libraries.spring.mockmvc.HttpResult.OK_NO_BODY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ValidGroupingControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingGrouping_shouldCreateNewGrouping() throws Exception {
        additionalSetup();

        ValidGroupingDTO grouping = Fakes.grouping(ADDITIONAL_GROUP_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, grouping, OK_NO_BODY);

        assertInDatabase(grouping);
    }

    @Test
    public void insertingGroupingForNonExistentGroup_shouldReturn404() throws Exception {
        ValidGroupingDTO grouping = Fakes.grouping(NEW_GROUP_ID, OLD_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, grouping, BAD, status().isNotFound());

        assertNotInDatabase(grouping);
    }

    @Test
    public void insertingGroupingForNonExistentStudent_shouldReturn404() throws Exception {
        ValidGroupingDTO grouping = Fakes.grouping(OLD_GROUP_ID, NEW_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, grouping, BAD, status().isNotFound());

        assertInDatabase(Fakes.grouping(OLD_GROUP_ID));
    }

    @Test
    public void insertingGroupingWhenGroupingAlreadyExists_shouldReturn409() throws Exception {
        additionalSetup();

        ValidGroupingDTO grouping = Fakes.grouping(OLD_GROUP_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPost(DEFAULT_PATH, grouping, BAD, status().isConflict());
    }

    @Test
    public void updatingGrouping_shouldUpdateGrouping() throws Exception {
        additionalSetup();

        ValidGroupingDTO grouping = Fakes.grouping(OLD_GROUP_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, grouping, OK_NO_BODY);

        assertInDatabase(grouping);
    }

    @Test
    public void updatingGroupingForNonExistentGroup_shouldReturn404() throws Exception {
        ValidGroupingDTO grouping = Fakes.grouping(NEW_GROUP_ID, OLD_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, grouping, BAD, status().isNotFound());

        assertNotInDatabase(grouping);
    }

    @Test
    public void updatingGroupingForNonExistentStudent_shouldReturn404() throws Exception {
        ValidGroupingDTO grouping = Fakes.grouping(OLD_GROUP_ID, NEW_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, grouping, BAD, status().isNotFound());

        assertInDatabase(Fakes.grouping(OLD_GROUP_ID));
    }

    @Test
    public void updatingGroupingWhenGroupingDoesNotExist_shouldReturn404() throws Exception {
        additionalSetup();

        ValidGroupingDTO grouping = Fakes.grouping(ADDITIONAL_GROUP_ID, ADDITIONAL_STUDENT_ID);

        mvc.performPut(DEFAULT_PATH, grouping, BAD, status().isNotFound());

        assertNotInDatabase(grouping);
    }

    @Test
    public void deletingGroupingForGroup_shouldDeleteGrouping() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "group/" + OLD_GROUP_ID, OK_NO_BODY);

        assertNotInDatabaseForGroupId(OLD_GROUP_ID);
    }

    @Test
    public void deletingGroupingForNonExistentGroup_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "group/" + NEW_GROUP_ID, BAD, status().isNotFound());

        assertNotInDatabaseForGroupId(NEW_GROUP_ID);
    }

    @Test
    public void deletingGroupingForGroupWithoutGrouping_shouldReturn404() throws Exception {
        additionalSetup();

        mvc.performDelete(DEFAULT_PATH + "group/" + ADDITIONAL_GROUP_ID, BAD, status().isNotFound());

        assertNotInDatabaseForGroupId(ADDITIONAL_GROUP_ID);
    }

    @Test
    public void deletingGroupingForStudent_shouldDeleteGrouping() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "student/" + OLD_STUDENT_ID, OK_NO_BODY);

        assertNotInDatabaseForStudentId(OLD_STUDENT_ID);
    }

    @Test
    public void deletingGroupingForNonExistentStudent_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + "student/" + NEW_STUDENT_ID, BAD, status().isNotFound());

        assertNotInDatabaseForStudentId(NEW_STUDENT_ID);
    }

    @Test
    public void deletingGroupingForStudentWithoutGrouping_shouldReturn404() throws Exception {
        additionalSetup();

        mvc.performDelete(DEFAULT_PATH + "student/" + ADDITIONAL_STUDENT_ID, BAD, status().isNotFound());

        assertNotInDatabaseForStudentId(ADDITIONAL_STUDENT_ID);
    }

    // PRIVATE

    private void assertInDatabase(ValidGroupingDTO grouping) {
        assertInDatabase(DB::readValidGrouping, ValidGroupingDTO::getGroupId, grouping);
    }

    private void assertNotInDatabase(ValidGroupingDTO grouping) {
        assertNotInDatabase(DB::readValidGrouping, grouping.getGroupId());
    }

    private void assertNotInDatabaseForGroupId(int groupId) {
        assertNotInDatabase(DB::readValidGrouping, groupId);
    }

    private void assertNotInDatabaseForStudentId(int studentId) {
        assertNotInDatabaseSecondary(DB::existsGroupingForStudentId, studentId);
    }

    private void additionalSetup() {
        db.insertValidGroup(Fakes.group(ADDITIONAL_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID));
        db.insertValidStudent(Fakes.student(ADDITIONAL_STUDENT_ID, null));
    }

    private static final int ADDITIONAL_GROUP_ID = 3;
    private static final int ADDITIONAL_STUDENT_ID = 3;

    private static final String DEFAULT_PATH = "/timestar/api/v2/grouping/";

}
