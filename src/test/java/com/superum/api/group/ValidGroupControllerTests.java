package com.superum.api.group;

import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.db.teacher.Teacher;
import env.IntegrationTestEnvironment;
import org.jooq.lambda.Seq;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.superum.utils.FakeFieldUtils.fakeLanguageLevel;
import static com.superum.utils.FakeFieldUtils.fakeName;
import static com.superum.utils.FakeUtils.*;
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
public class ValidGroupControllerTests extends IntegrationTestEnvironment {

    @Test
    public void insertingGroupWithoutId_shouldCreateNewGroup() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId()).withoutId();

        MvcResult result = mockMvc.perform(put("/timestar/api/v2/group/")
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader())
                    .content(convertObjectToJsonBytes(group)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        ValidGroupDTO returnedGroup = fromResponse(result, ValidGroupDTO.class);
        int groupId = returnedGroup.getId();
        group = group.withId(groupId);

        assertEquals("The returned group should be equal to original group, except for id field; ", returnedGroup, group);

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);

        assertEquals("The group in the database should be equal to the returned group; ", groupFromDB, returnedGroup);
    }

    @Test
    public void readingGroupWithValidId_shouldReturnAGroup() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/group/{groupId}", groupId)
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        ValidGroupDTO returnedGroup = fromResponse(result, ValidGroupDTO.class);

        assertEquals("The read group should be equal to original group; ", returnedGroup, insertedGroup);

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);

        assertEquals("The group in the database should be equal to the returned group; ", groupFromDB, returnedGroup);
    }

    @Test
    public void updatingGroupWithValidData_shouldReturnOldGroup() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();

        ValidGroupDTO updatedGroup = makeFakeValidGroup(GROUP_SEED + 1, validCustomerDTO.getId(), teacher.getId()).withId(groupId);

        mockMvc.perform(post("/timestar/api/v2/group/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(updatedGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);

        assertEquals("The group in the database should be equal to the updated group; ", groupFromDB, updatedGroup);
    }

    @Test
    public void updatingPartialGroupWithValidData_shouldReturnOldGroup() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();

        ValidGroupDTO partialUpdatedGroup = makeFakeValidGroup(groupId, null, null, null,
                fakeLanguageLevel(GROUP_SEED + 1), fakeName(GROUP_SEED + 1));

        mockMvc.perform(post("/timestar/api/v2/group/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(partialUpdatedGroup)))
                .andDo(print())
                .andExpect(status().isOk());

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);
        ValidGroupDTO updatedGroup = makeFakeValidGroup(groupId, insertedGroup.getCustomerId(),
                insertedGroup.getTeacherId(), insertedGroup.getUsesHourlyWage(),
                partialUpdatedGroup.getLanguageLevel(), partialUpdatedGroup.getName());

        assertEquals("The group in the database should be equal to the updated group; ", groupFromDB, updatedGroup);
    }

    @Test
    public void updatingGroupWithInvalidId_shouldReturnBadRequest() throws Exception {
        ValidGroupDTO validGroup = makeFakeValidGroup(GROUP_SEED);

        String json = convertObjectToString(validGroup);
        String invalidJson = replace(json, "id", -1);

        mockMvc.perform(post("/timestar/api/v2/group/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletingGroupWithValidId_shouldReturnDeletedGroup() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();

        mockMvc.perform(delete("/timestar/api/v2/group/{groupId}", groupId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk());

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);

        assertNull("The group from the database should be equal null, since it was deleted; ", groupFromDB);
    }

    @Test
    public void readingAllGroups_shouldReturnListOfGroups() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        List<ValidGroupDTO> allGroups = Seq.of(group)
                .map(ValidGroupDTO::withoutId)
                .map(databaseHelper::insertValidGroupIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/group/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<ValidGroupDTO> returnedGroups = fromResponse(result, LIST_OF_GROUPS);

        assertEquals("The read groups should be equal to original groups; ", returnedGroups, allGroups);

        List<ValidGroupDTO> groupsFromDb = allGroups.stream()
                .mapToInt(ValidGroupDTO::getId)
                .mapToObj(databaseHelper::readValidGroupFromDb)
                .collect(Collectors.toList());

        assertEquals("The groups in the database should be equal to the read groups; ", groupsFromDb, returnedGroups);
    }

    @Test
    public void readingGroupForTeacherWithValidId_shouldReturnListOfGroups() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();
        List<ValidGroupDTO> validGroups = Collections.singletonList(insertedGroup);

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/group/{tableName}/{teacherId}", "teacher", teacher.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<ValidGroupDTO> returnedGroups = fromResponse(result, LIST_OF_GROUPS);

        assertEquals("The read groups should be equal to original groups; ", returnedGroups, validGroups);

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);
        List<ValidGroupDTO> groupsFromDb = Collections.singletonList(groupFromDB);

        assertEquals("The groups in the database should be equal to the read groups; ", groupsFromDb, returnedGroups);
    }

    @Test
    public void readingGroupForCustomerWithValidId_shouldReturnListOfGroups() throws Exception {
        ValidCustomerDTO validCustomerDTO = databaseHelper.insertFullCustomerIntoDb(makeFakeFullCustomer(GROUP_SEED));
        Teacher teacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(GROUP_SEED));
        ValidGroupDTO group = makeFakeValidGroup(GROUP_SEED, validCustomerDTO.getId(), teacher.getId());

        ValidGroupDTO insertedGroup = databaseHelper.insertValidGroupIntoDb(group);
        int groupId = insertedGroup.getId();
        List<ValidGroupDTO> validGroups = Collections.singletonList(insertedGroup);

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/group/{tableName}/{customerId}", "customer", validCustomerDTO.getId())
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<ValidGroupDTO> returnedGroups = fromResponse(result, LIST_OF_GROUPS);

        assertEquals("The read groups should be equal to original groups; ", returnedGroups, validGroups);

        ValidGroupDTO groupFromDB = databaseHelper.readValidGroupFromDb(groupId);
        List<ValidGroupDTO> groupsFromDb = Collections.singletonList(groupFromDB);

        assertEquals("The groups in the database should be equal to the read groups; ", groupsFromDb, returnedGroups);
    }

    // PRIVATE

    private static final TypeReference<List<ValidGroupDTO>> LIST_OF_GROUPS = new TypeReference<List<ValidGroupDTO>>() {};
    private static final int GROUP_SEED = 1;

}
