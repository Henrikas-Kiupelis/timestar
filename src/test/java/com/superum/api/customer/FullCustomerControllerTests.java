package com.superum.api.customer;

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
import static com.superum.utils.FakeUtils.makeFakeFullCustomer;
import static com.superum.utils.FakeUtils.makeSomeFakes;
import static com.superum.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FullCustomerControllerTests extends IntegrationTestEnvironment {

    @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        FullCustomer customer = makeFakeFullCustomer(CUSTOMER_SEED).withoutId();

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

    @Test
    public void readingCustomerWithValidId_shouldReturnACustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
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

    @Test
    public void updatingCustomerWithValidData_shouldReturnOldCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        FullCustomer updatedCustomer = makeFakeFullCustomer(CUSTOMER_SEED + 1).withId(customerId);

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

    @Test
    public void updatingPartialCustomerWithValidData_shouldReturnOldCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        FullCustomer partialUpdatedCustomer = makeFakeFullCustomer(customerId, null,
                fakeName(CUSTOMER_SEED + 1), fakePhone(CUSTOMER_SEED + 1),
                null, null, null);

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/customer/update")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(partialUpdatedCustomer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result, FullCustomer.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = databaseHelper.readCustomerFromDb(customerId);
        FullCustomer updatedCustomer = makeFakeFullCustomer(customerId, insertedCustomer.getStartDate(),
                partialUpdatedCustomer.getName(), partialUpdatedCustomer.getPhone(),
                insertedCustomer.getWebsite(), insertedCustomer.getPicture(), insertedCustomer.getComment());

        assertEquals("The customer in the database should be equal to the updated customer; ", customerFromDB, updatedCustomer);
    }

    @Test
    public void deletingCustomerWithValidId_shouldReturnDeletedCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
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

    @Test
    public void readingCustomerForTeacherWithValidId_shouldReturnListOfCustomers() throws Exception {
        // THIS TEST NEEDS TO BE REWRITTEN DUE TO SCHEMA CHANGES
        /*
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();
        List<FullCustomer> validCustomers = Collections.singletonList(insertedCustomer);

        Teacher insertedTeacher = databaseHelper.insertTeacherIntoDb(makeFakeTeacher(0));
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

        assertEquals("The customers in the database should be equal to the read customers; ", customersFromDb, returnedCustomers);*/
    }

    @Test
    public void readingAllCustomers_shouldReturnListOfCustomers() throws Exception {
        List<FullCustomer> allCustomers = makeSomeFakes(2, FakeUtils::makeFakeFullCustomer).stream()
                .map(FullCustomer::withoutId)
                .map(databaseHelper::insertCustomerIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/all")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<FullCustomer> returnedCustomers = fromResponse(result, LIST_OF_CUSTOMERS);

        assertEquals("The read customers should be equal to original customers; ", returnedCustomers, allCustomers);

        List<FullCustomer> customersFromDb = allCustomers.stream()
                .mapToInt(FullCustomer::getId)
                .mapToObj(databaseHelper::readCustomerFromDb)
                .filter(customer -> customer != null)
                .collect(Collectors.toList());

        assertEquals("The customers in the database should be equal to the read customers; ", customersFromDb, returnedCustomers);
    }

    @Test
    public void countingCustomersForTeacherWithValidId_shouldReturnCount() throws Exception {
        // THIS TEST NEEDS TO BE REWRITTEN DUE TO SCHEMA CHANGES
        /*
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
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

        assertEquals("The amount of customers from the database should be equal to the amount of read customers; ", customersFromDb.size(), returnedCount);*/
    }

    @Test
    public void countingAllCustomers_shouldReturnCount() throws Exception {
        List<FullCustomer> allCustomers = makeSomeFakes(2, FakeUtils::makeFakeFullCustomer).stream()
                .map(FullCustomer::withoutId)
                .map(databaseHelper::insertCustomerIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/all/count")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount = fromResponse(result, Integer.class);

        assertEquals("The amount of read customers should be equal to original amount of customers; ", returnedCount, allCustomers.size());

        long amountOfCustomersInDb = allCustomers.stream()
                .mapToInt(FullCustomer::getId)
                .mapToObj(databaseHelper::readCustomerFromDb)
                .filter(customer -> customer != null)
                .count();

        assertEquals("The amount of customers in the database should be equal to the amount of read customers; ", amountOfCustomersInDb, returnedCount);
    }

    @Test
    public void doesCustomerExist_shouldReturnExistingCustomer() throws Exception {
        FullCustomer insertedCustomer = databaseHelper.insertCustomerIntoDb(makeFakeFullCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        System.out.println("Before JSON: " + insertedCustomer);
        byte[] json = convertObjectToJsonBytes(insertedCustomer);
        String jsonStr1 = convertBytesToObject(json, FullCustomer.class).toString();
        System.out.println("Byte JSON: " + jsonStr1);
        String jsonStr2 = convertObjectToString(insertedCustomer);
        System.out.println("String JSON: " + jsonStr2);

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
    private static final int CUSTOMER_SEED = 1;

}
