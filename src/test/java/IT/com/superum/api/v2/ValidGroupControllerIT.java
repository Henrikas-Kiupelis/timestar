package IT.com.superum.api.v2;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.v2.group.ValidGroupDTO;
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
public class ValidGroupControllerIT extends IntegrationTestEnvironment {

    @Test
    public void creatingGroupWithoutId_shouldCreateNewGroup() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID).withoutId();

        ValidGroupDTO insertedGroup = mvc.performPost(DEFAULT_PATH, group, OK)
                .map(Unchecked.function(this::readGroup))
                .orElseThrow(() -> new Exception("Successful insertion should return a group!"));

        assertEquals("Inserted group should be equal to the original (except id)",
                group.withId(insertedGroup.getId()), insertedGroup);

        assertInDatabase(insertedGroup);
    }

    @Test
    public void creatingGroupWithId_shouldReturn400() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID);

        mvc.performPost(DEFAULT_PATH, group, BAD, status().isBadRequest());

        assertNotInDatabase(group);
    }

    @Test
    public void creatingGroupWithoutName_shouldReturn400() throws Exception {
        ValidGroupDTO group = ValidGroupDTO.builder()
                .teacherId(OLD_TEACHER_ID)
                .usesHourlyWage(Fake.Boolean(NEW_GROUP_ID))
                .languageLevel(Fake.languageLevel(NEW_GROUP_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, group, BAD, status().isBadRequest());
    }

    @Test
    public void creatingGroupWithNonExistentTeacherId_shouldReturn404() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, NEW_TEACHER_ID, OLD_CUSTOMER_ID).withoutId();

        mvc.performPost(DEFAULT_PATH, group, BAD, status().isNotFound());
    }

    @Test
    public void creatingGroupWithNonExistentCustomerId_shouldReturn404() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, NEW_CUSTOMER_ID).withoutId();

        mvc.performPost(DEFAULT_PATH, group, BAD, status().isNotFound());
    }

    @Test
    public void updatingGroupWithId_shouldUpdateGroup() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID).withId(OLD_GROUP_ID);

        mvc.performPut(DEFAULT_PATH, group, OK_NO_BODY);

        assertInDatabase(group);
    }

    @Test
    public void updatingGroupWithIdAndNameOnly_shouldUpdateGroup() throws Exception {
        ValidGroupDTO partialGroup = ValidGroupDTO.builder()
                .id(OLD_GROUP_ID)
                .name(Fake.name(NEW_GROUP_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, partialGroup, OK_NO_BODY);

        ValidGroupDTO beforeUpdate = Fakes.group(OLD_GROUP_ID);
        ValidGroupDTO afterUpdate = ValidGroupDTO.stepBuilder()
                .teacherId(beforeUpdate.getTeacherId())
                .usesHourlyWage(beforeUpdate.getUsesHourlyWage())
                .languageLevel(beforeUpdate.getLanguageLevel())
                .name(partialGroup.getName())
                .id(partialGroup.getId())
                .customerId(beforeUpdate.getCustomerId())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingGroupWithoutId_shouldReturn400() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID).withoutId();

        mvc.performPut(DEFAULT_PATH, group, BAD, status().isBadRequest());
    }

    @Test
    public void updatingGroupWithOnlyId_shouldReturn400() throws Exception {
        ValidGroupDTO group = ValidGroupDTO.builder()
                .id(OLD_GROUP_ID)
                .build();

        mvc.performPut(DEFAULT_PATH, group, BAD, status().isBadRequest());

        assertInDatabase(Fakes.group(OLD_GROUP_ID));
    }

    @Test
    public void updatingGroupWithNonExistentId_shouldReturn404() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID);

        mvc.performPut(DEFAULT_PATH, group, BAD, status().isNotFound());

        assertNotInDatabase(group);
    }

    @Test
    public void updatingGroupWithNonExistentTeacherId_shouldReturn404() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, NEW_TEACHER_ID, OLD_CUSTOMER_ID).withId(OLD_GROUP_ID);

        mvc.performPut(DEFAULT_PATH, group, BAD, status().isNotFound());

        assertInDatabase(Fakes.group(OLD_GROUP_ID));
    }

    @Test
    public void updatingGroupWithNonExistentCustomerId_shouldReturn404() throws Exception {
        ValidGroupDTO group = Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, NEW_CUSTOMER_ID).withId(OLD_GROUP_ID);

        mvc.performPut(DEFAULT_PATH, group, BAD, status().isNotFound());

        assertInDatabase(Fakes.group(OLD_GROUP_ID));
    }

    @Test
    public void deletingGroupById_shouldDeleteGroup() throws Exception {
        ValidGroupDTO group = db.insertValidGroup(Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, OLD_CUSTOMER_ID));

        mvc.performDelete(DEFAULT_PATH + group.getId(), OK_NO_BODY);

        assertNotInDatabase(group);
    }

    @Test
    public void deletingGroupByStillUsedId_shouldReturn400() throws Exception {
        mvc.performDelete(DEFAULT_PATH + OLD_GROUP_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_GROUP_ID);
    }

    @Test
    public void deletingGroupByNonExistentId_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + NEW_GROUP_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_GROUP_ID);
    }

    @Test
    public void readingGroupById_shouldReturnGroup() throws Exception {
        ValidGroupDTO group = mvc.performGet(DEFAULT_PATH + OLD_GROUP_ID, OK)
                .map(Unchecked.function(this::readGroup))
                .orElseThrow(() -> new Exception("Successful read should return a group!"));

        assertInDatabase(group);
    }

    @Test
    public void readingGroupByNonExistentId_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + NEW_GROUP_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingAllGroups_shouldReturnGroups() throws Exception {
        List<ValidGroupDTO> groups = mvc.performGet(DEFAULT_PATH, OK)
                .map(Unchecked.function(this::readGroups))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, groups.size());
        groups.forEach(this::assertInDatabase);
    }

    @Test
    public void readingGroupsForTeacher_shouldReturnGroups() throws Exception {
        List<ValidGroupDTO> groups = mvc.performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID, OK)
                .map(Unchecked.function(this::readGroups))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, groups.size());
        groups.forEach(this::assertInDatabase);
    }

    @Test
    public void readingGroupsForNonExistentTeacher_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingGroupsForCustomer_shouldReturnGroups() throws Exception {
        List<ValidGroupDTO> groups = mvc.performGet(DEFAULT_PATH + "customer/" + OLD_CUSTOMER_ID, OK)
                .map(Unchecked.function(this::readGroups))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, groups.size());
        groups.forEach(this::assertInDatabase);
    }

    @Test
    public void readingGroupsForNonExistentCustomer_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "customer/" + NEW_CUSTOMER_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingGroupsForStudent_shouldReturnGroups() throws Exception {
        List<ValidGroupDTO> groups = mvc.performGet(DEFAULT_PATH + "student/" + OLD_STUDENT_ID, OK)
                .map(Unchecked.function(this::readGroups))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, groups.size());
        groups.forEach(this::assertInDatabase);
    }

    @Test
    public void readingGroupsForNonExistentStudent_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "student/" + NEW_STUDENT_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingGroupsWithoutCustomer_shouldReturnGroups() throws Exception {
        db.insertValidGroup(Fakes.group(NEW_GROUP_ID, OLD_TEACHER_ID, null));

        List<ValidGroupDTO> groups = mvc.performGet(DEFAULT_PATH + "customer/none", OK)
                .map(Unchecked.function(this::readGroups))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, groups.size());
        groups.forEach(this::assertInDatabase);
    }

    // PRIVATE

    private ValidGroupDTO readGroup(MvcResult result) throws IOException {
        return MVC.from(result).to(ValidGroupDTO.class);
    }

    private List<ValidGroupDTO> readGroups(MvcResult result) throws IOException {
        return MVC.from(result).to(LIST_OF_GROUPS);
    }

    private void assertNotInDatabase(ValidGroupDTO group) {
        assertNotInDatabase(DB::readValidGroup, group.getId());
    }

    private void assertNotInDatabase(int groupId) {
        assertNotInDatabase(DB::readValidGroup, groupId);
    }

    private void assertInDatabase(ValidGroupDTO group) {
        assertInDatabase(DB::readValidGroup, ValidGroupDTO::getId, group);
    }

    private void assertInDatabase(int groupId) {
        assertInDatabase(DB::readValidGroup, groupId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/group/";

    private static final TypeReference<List<ValidGroupDTO>> LIST_OF_GROUPS = new TypeReference<List<ValidGroupDTO>>() {};

}
