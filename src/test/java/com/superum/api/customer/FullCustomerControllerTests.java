package com.superum.api.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.db.group.Group;
import com.superum.db.group.student.Student;
import com.superum.db.teacher.Teacher;
import env.IntegrationTestEnvironment;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.superum.utils.FakeUtils.defaultFullCustomer;
import static com.superum.utils.FakeUtils.defaultTeacher;
import static com.superum.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FullCustomerControllerTests extends IntegrationTestEnvironment {

    @Transactional @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        FullCustomer customer = defaultFullCustomer();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/customer/create")
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader())
                    .content(convertObjectToJsonBytes(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);
        int customerId = returnedCustomer.getId();

        customer = customer.withId(customerId);

        assertEquals("The returned customer should be equal to original customer, except for id field; ", returnedCustomer, customer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    @Transactional @Test
    public void readingCustomerWithValidId_shouldReturnACustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/{customerId}", customerId)
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    @Transactional @Test
    public void updatingCustomerWithValidData_shouldReturnOldCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();

        FullCustomer updatedCustomer = FullCustomer.newBuilder()
                .withId(customerId)
                .withPaymentDay(2)
                .withStartDate(Date.valueOf("2015-07-29"))
                .withPaymentValue(BigDecimal.valueOf(20))
                .withName("SUPERUMZ")
                .withPhone("+37069900002")
                .withWebsite("https://superum.eu/")
                .withLanguages(Arrays.asList("English: C1", "English: C2", "English: C3"))
                .withPictureName("picture.jpg")
                .withComment("company updated test")
                .build();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/customer/update")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(updatedCustomer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the updated customer; ", customerFromDB, updatedCustomer);
    }

    @Transactional @Test
    public void deletingCustomerWithValidId_shouldReturnDeletedCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();

        MvcResult result = mockMvc.perform(delete("/timestar/api/v2/customer/delete/{customerId}", customerId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);

        assertNull("The customer from the database should be equal null, since it was deleted; ", customerFromDB);
    }

    @Transactional @Test
    public void readingCustomerForTeacherWithValidId_shouldReturnListOfCustomers() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();
        List<FullCustomer> validCustomers = Collections.singletonList(insertedCustomer);

        Teacher insertedTeacher = databaseHelper.insertTeacherIntoDb(defaultTeacher());
        int teacherId = insertedTeacher.getId();

        Group insertedGroup = databaseHelper.insertGroupIntoDb(new Group(0, teacherId, "GroupZ"));
        int groupId = insertedGroup.getId();

        databaseHelper.insertStudentIntoDb(new Student(0, groupId, customerId, "djsdasuodhnsauo@DUOSAODusa.asdbiuasd", "blegh"));

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/for/teacher/{teacherId}", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<FullCustomer> returnedCustomers = fromResponse(result, LIST_OF_CUSTOMERS);

        assertEquals("The read customers should be equal to original customers; ", returnedCustomers, validCustomers);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);
        List<FullCustomer> customersFromDb = Collections.singletonList(customerFromDB);

        assertEquals("The customers in the database should be equal to the read customers; ", customersFromDb, returnedCustomers);
    }

    @Transactional @Test
    public void readingAllCustomers_shouldReturnListOfCustomers() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();
        List<FullCustomer> allCustomers = Collections.singletonList(insertedCustomer);

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/all")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<FullCustomer> returnedCustomers = fromResponse(result, LIST_OF_CUSTOMERS);

        assertEquals("The read customers should be equal to original customers; ", returnedCustomers, allCustomers);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);
        List<FullCustomer> customersFromDb = Collections.singletonList(customerFromDB);

        assertEquals("The customers in the database should be equal to the read customers; ", customersFromDb, returnedCustomers);
    }

    @Transactional @Test
    public void countingCustomersForTeacherWithValidId_shouldReturnCount() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();
        List<FullCustomer> validCustomers = Collections.singletonList(insertedCustomer);

        Teacher insertedTeacher = databaseHelper.insertTeacherIntoDb(defaultTeacher());
        int teacherId = insertedTeacher.getId();

        Group insertedGroup = databaseHelper.insertGroupIntoDb(new Group(0, teacherId, "GroupZ"));
        int groupId = insertedGroup.getId();

        databaseHelper.insertStudentIntoDb(new Student(0, groupId, customerId, "djsdasuodhnsauo@DUOSAODusa.asdbiuasd", "blegh"));

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/for/teacher/{teacherId}/count", teacherId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount = fromResponse(result, Integer.class);

        assertEquals("The amount of read customers should be equal to original amount of customers; ", returnedCount, validCustomers.size());

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);
        List<FullCustomer> customersFromDb = Collections.singletonList(customerFromDB);

        assertEquals("The amount of customers from the database should be equal to the amount of read customers; ", customersFromDb.size(), returnedCount);
    }

    @Transactional @Test
    public void countingAllCustomers_shouldReturnCount() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();
        List<FullCustomer> allCustomers = Collections.singletonList(insertedCustomer);

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/all/count")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount  = fromResponse(result, Integer.class);

        assertEquals("The amount of read customers should be equal to original amount of customers; ", returnedCount, allCustomers.size());

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);
        List<FullCustomer> customersFromDb = Collections.singletonList(customerFromDB);

        assertEquals("The amount of customers in the database should be equal to the amount of read customers; ", customersFromDb.size(), returnedCount);
    }

    @Transactional @Test
    public void doesCustomerExist_shouldReturnExistingCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(defaultFullCustomer());
        int customerId = insertedCustomer.getId();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/customer/exists")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(insertedCustomer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    // PRIVATE

    private static final TypeReference<List<FullCustomer>> LIST_OF_CUSTOMERS = new TypeReference<List<FullCustomer>>() {};

}
