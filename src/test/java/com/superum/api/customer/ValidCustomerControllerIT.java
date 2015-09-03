package com.superum.api.customer;

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
import java.util.function.BiPredicate;

import static com.superum.helper.ResultVariation.*;
import static com.superum.utils.MockMvcUtils.fromResponse;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ValidCustomerControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        ValidCustomerDTO customer = Fake.customer(NEW_CUSTOMER_ID).withoutId();

        ValidCustomerDTO insertedCustomer = performPut(DEFAULT_PATH, customer, OK)
                .map(Unchecked.function(this::readCustomer))
                .orElseThrow(() -> new Exception("Successful insertion should return a customer!"));

        assertEquals("Inserted customer should be equal to the original (except id)",
                customer.withId(insertedCustomer.getId()), insertedCustomer);

        assertInDatabase(insertedCustomer);
    }

    @Test
    public void insertingCustomerWithId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = Fake.customer(NEW_CUSTOMER_ID);

        performPut(DEFAULT_PATH, customer, BAD, status().isBadRequest());

        assertNotInDatabase(customer);
    }

    @Test
    public void insertingCustomerWithoutName_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = ValidCustomerDTO.builder()
                .startDate(Fake.localDate(NEW_CUSTOMER_ID))
                .phone(Fake.phone(NEW_CUSTOMER_ID))
                .website(Fake.website(NEW_CUSTOMER_ID))
                .build();

        performPut(DEFAULT_PATH, customer, BAD, status().isBadRequest());
    }

    @Test
    public void updatingCustomerWithId_shouldUpdateCustomer() throws Exception {
        ValidCustomerDTO customer = Fake.customer(NEW_CUSTOMER_ID).withId(OLD_CUSTOMER_ID);

        performPost(DEFAULT_PATH, customer, OK_NO_BODY);

        assertInDatabase(customer);
    }

    @Test
    public void updatingCustomerWithIdAndNameOnly_shouldUpdateCustomer() throws Exception {
        ValidCustomerDTO partialCustomer = ValidCustomerDTO.builder()
                .id(OLD_CUSTOMER_ID)
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        performPost(DEFAULT_PATH, partialCustomer, OK_NO_BODY);

        ValidCustomerDTO beforeUpdate = Fake.customer(OLD_CUSTOMER_ID);
        ValidCustomerDTO afterUpdate = ValidCustomerDTO.builder()
                .id(partialCustomer.getId())
                .startDate(beforeUpdate.getStartDate())
                .name(partialCustomer.getName())
                .phone(beforeUpdate.getPhone())
                .website(beforeUpdate.getWebsite())
                .picture(beforeUpdate.getPicture())
                .comment(beforeUpdate.getComment())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingCustomerWithoutId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = Fake.customer(NEW_CUSTOMER_ID).withoutId();

        performPost(DEFAULT_PATH, customer, BAD, status().isBadRequest());
    }

    @Test
    public void updatingCustomerWithOnlyId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = ValidCustomerDTO.builder()
                .id(OLD_CUSTOMER_ID)
                .build();

        performPost(DEFAULT_PATH, customer, BAD, status().isBadRequest());

        assertInDatabase(Fake.customer(OLD_CUSTOMER_ID));
    }

    @Test
    public void updatingCustomerWithNonExistentId_shouldReturn404() throws Exception {
        ValidCustomerDTO customer = Fake.customer(NEW_CUSTOMER_ID);

        performPost(DEFAULT_PATH, customer, BAD, status().isNotFound());

        assertNotInDatabase(customer);
    }

    @Test
    public void deletingCustomerById_shouldDeleteCustomer() throws Exception {
        ValidCustomerDTO customer = db.insertValidCustomer(Fake.customer(NEW_CUSTOMER_ID));

        performDelete(DEFAULT_PATH + NEW_CUSTOMER_ID, OK_NO_BODY);

        assertNotInDatabase(customer);
    }

    @Test
    public void deletingCustomerByStillUsedId_shouldReturn400() throws Exception {
        performDelete(DEFAULT_PATH + OLD_CUSTOMER_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_CUSTOMER_ID);
    }

    @Test
    public void deletingCustomerByNonExistentId_shouldReturn404() throws Exception {
        performDelete(DEFAULT_PATH + NEW_CUSTOMER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_CUSTOMER_ID);
    }

    @Test
    public void readingCustomerById_shouldReturnCustomer() throws Exception {
        ValidCustomerDTO customer = performGet(DEFAULT_PATH + OLD_CUSTOMER_ID, OK)
                .map(Unchecked.function(this::readCustomer))
                .orElseThrow(() -> new Exception("Successful read should return a customer!"));

        assertInDatabase(customer);
    }

    @Test
    public void readingCustomerByNonExistentId_shouldReturn404() throws Exception {
        performGet(DEFAULT_PATH + NEW_CUSTOMER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_CUSTOMER_ID);
    }

    @Test
    public void readingAllCustomers_shouldReturnCustomers() throws Exception {
        List<ValidCustomerDTO> customers = performGet(DEFAULT_PATH, OK)
                .map(Unchecked.function(this::readCustomers))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, customers.size());
        customers.forEach(this::assertInDatabase);
    }

    @Test
    public void countingAllCustomers_shouldReturnCountOf2() throws Exception {
        int count = performGet(DEFAULT_PATH + "count", OK)
                .map(Unchecked.function(this::readCount))
                .orElseThrow(() -> new Exception("Successful read should return an integer!"));

        assertEquals("When counting all, 2 should be returned", 2, count);
    }

    @Test
    public void readingCustomersForTeacher_shouldReturnCustomers() throws Exception {
        List<ValidCustomerDTO> customers = performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID, OK)
                .map(Unchecked.function(this::readCustomers))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, customers.size());
        customers.forEach(this::assertInDatabase);
    }

    @Test
    public void readingCustomersForNonExistentTeacher_shouldReturn404() throws Exception {
        performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_TEACHER_ID);
    }

    @Test
    public void countingCustomersForTeacher_shouldReturnCountOf1() throws Exception {
        int count = performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID + "/count", OK)
                .map(Unchecked.function(this::readCount))
                .orElseThrow(() -> new Exception("Successful read should return an integer!"));

        assertEquals("When counting for something, 1 should be returned", 1, count);
    }

    @Test
    public void countingCustomersForNonExistentTeacher_shouldReturn404() throws Exception {
        performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID + "/count", BAD, status().isNotFound());

        assertNotInDatabase(NEW_TEACHER_ID);
    }

    // PRIVATE

    private ValidCustomerDTO readCustomer(MvcResult result) throws IOException {
        return fromResponse(result, ValidCustomerDTO.class);
    }

    private List<ValidCustomerDTO> readCustomers(MvcResult result) throws IOException {
        return fromResponse(result, LIST_OF_CUSTOMERS);
    }

    private void assertNotInDatabase(ValidCustomerDTO customer) {
        assertNotInDatabase(DB::readValidCustomer, customer.getId());
    }

    private void assertNotInDatabase(int customerId) {
        assertNotInDatabase(DB::readValidCustomer, customerId);
    }

    private void assertInDatabase(ValidCustomerDTO customer) {
        assertInDatabase(DB::readValidCustomer, ValidCustomerDTO::getId, customer);
    }

    private void assertInDatabase(ValidCustomerDTO customer, BiPredicate<ValidCustomerDTO, ValidCustomerDTO> customEqualityCheck) {
        assertInDatabase(DB::readValidCustomer, ValidCustomerDTO::getId, customer, customEqualityCheck);
    }

    private void assertInDatabase(int customerId) {
        assertInDatabase(DB::readValidCustomer, customerId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/customer/";

    private static final TypeReference<List<ValidCustomerDTO>> LIST_OF_CUSTOMERS = new TypeReference<List<ValidCustomerDTO>>() {};

   /* @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        ValidCustomerDTO customer = makeFakeValidCustomer(CUSTOMER_SEED).withoutId();

        MvcResult result = mockMvc.perform(put("/timestar/api/v2/customer/")
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader())
                    .content(convertObjectToJsonBytes(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        ValidCustomerDTO returnedCustomer = fromResponse(result, ValidCustomerDTO.class);
        int customerId = returnedCustomer.getId();
        customer = customer.withId(customerId);

        assertEquals("The returned customer should be equal to original customer, except for id field; ", returnedCustomer, customer);

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    @Test
    public void readingCustomerWithValidId_shouldReturnACustomer() throws Exception {
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/{customerId}", customerId)
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        ValidCustomerDTO returnedCustomer = fromResponse(result, ValidCustomerDTO.class);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    @Test
    public void updatingCustomerWithValidData_shouldReturnOldCustomer() throws Exception {
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        ValidCustomerDTO updatedCustomer = makeFakeValidCustomer(CUSTOMER_SEED + 1).withId(customerId);

        mockMvc.perform(post("/timestar/api/v2/customer")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(updatedCustomer)))
                .andDo(print())
                .andExpect(status().isOk());

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);

        assertEquals("The customer in the database should be equal to the updated customer; ", customerFromDB, updatedCustomer);
    }

    @Test
    public void updatingPartialCustomerWithValidData_shouldReturnOldCustomer() throws Exception {
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        ValidCustomerDTO partialUpdatedCustomer = makeFakeValidCustomer(customerId, null,
                fakeName(CUSTOMER_SEED + 1), fakePhone(CUSTOMER_SEED + 1),
                null, null, null);

        mockMvc.perform(post("/timestar/api/v2/customer/")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(partialUpdatedCustomer)))
                .andDo(print())
                .andExpect(status().isOk());

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);
        ValidCustomerDTO updatedCustomer = makeFakeValidCustomer(customerId, insertedCustomer.getStartDate(),
                partialUpdatedCustomer.getName(), partialUpdatedCustomer.getPhone(),
                insertedCustomer.getWebsite(), insertedCustomer.getPicture(), insertedCustomer.getComment());

        assertEquals("The customer in the database should be equal to the updated customer; ", customerFromDB, updatedCustomer);
    }

    @Test
    public void updatingCustomerWithInvalidId_shouldReturnBadReques() throws Exception {
        ValidCustomerDTO validCustomer = makeFakeValidCustomer(CUSTOMER_SEED);

        String json = convertObjectToString(validCustomer);
        String invalidJson = replace(json, "id", -1);

        mockMvc.perform(post("/timestar/api/v2/customer")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(invalidJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deletingCustomerWithValidId_shouldReturnDeletedCustomer() throws Exception {
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();

        mockMvc.perform(delete("/timestar/api/v2/customer/{customerId}", customerId)
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk());

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);

        assertNull("The customer from the database should be equal null, since it was deleted; ", customerFromDB);
    }*/

    @Test
    public void readingCustomerForTeacherWithValidId_shouldReturnListOfCustomers() throws Exception {
        // THIS TEST NEEDS TO BE REWRITTEN DUE TO SCHEMA CHANGES
        /*
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();
        List<ValidCustomerDTO> validCustomers = Collections.singletonList(insertedCustomer);

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

        List<ValidCustomerDTO> returnedCustomers = fromResponse(result, LIST_OF_CUSTOMERS);

        assertEquals("The read customers should be equal to original customers; ", returnedCustomers, validCustomers);

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);
        List<ValidCustomerDTO> customers = Collections.singletonList(customerFromDB);

        assertEquals("The customers in the database should be equal to the read customers; ", customers, returnedCustomers);*/
    }

    /*@Test
    public void readingAllCustomers_shouldReturnListOfCustomers() throws Exception {
        List<ValidCustomerDTO> allCustomers = makeSomeFakes(2, FakeUtils::makeFakeValidCustomer).stream()
                .map(ValidCustomerDTO::withoutId)
                .map(databaseHelper::insertFullCustomerIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        List<ValidCustomerDTO> returnedCustomers = fromResponse(result, LIST_OF_CUSTOMERS);

        assertEquals("The read customers should be equal to original customers; ", returnedCustomers, allCustomers);

        List<ValidCustomerDTO> customers = allCustomers.stream()
                .mapToInt(ValidCustomerDTO::getId)
                .mapToObj(databaseHelper::readFullCustomer)
                .collect(Collectors.toList());

        assertEquals("The customers in the database should be equal to the read customers; ", customers, returnedCustomers);
    }*/

    @Test
    public void countingCustomersForTeacherWithValidId_shouldReturnCount() throws Exception {
        // THIS TEST NEEDS TO BE REWRITTEN DUE TO SCHEMA CHANGES
        /*
        ValidCustomerDTO insertedCustomer = databaseHelper.insertFullCustomerIntoDb(makeFakeValidCustomer(CUSTOMER_SEED).withoutId());
        int customerId = insertedCustomer.getId();
        List<ValidCustomerDTO> validCustomers = Collections.singletonList(insertedCustomer);

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

        ValidCustomerDTO customerFromDB = databaseHelper.readFullCustomer(customerId);
        List<ValidCustomerDTO> customers = Collections.singletonList(customerFromDB);

        assertEquals("The amount of customers from the database should be equal to the amount of read customers; ", customers.size(), returnedCount);*/
    }

    /*@Test
    public void countingAllCustomers_shouldReturnCount() throws Exception {
        List<ValidCustomerDTO> allCustomers = makeSomeFakes(2, FakeUtils::makeFakeValidCustomer).stream()
                .map(ValidCustomerDTO::withoutId)
                .map(databaseHelper::insertFullCustomerIntoDb)
                .collect(Collectors.toList());

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/count")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        int returnedCount = fromResponse(result, Integer.class);

        assertEquals("The amount of read customers should be equal to original amount of customers; ", returnedCount, allCustomers.size());

        long amountOfCustomersInDb = allCustomers.stream()
                .mapToInt(ValidCustomerDTO::getId)
                .mapToObj(databaseHelper::readFullCustomer)
                .filter(customer -> customer != null)
                .count();

        assertEquals("The amount of customers in the database should be equal to the amount of read customers; ", amountOfCustomersInDb, returnedCount);
    }

    // PRIVATE

    private static final TypeReference<List<ValidCustomerDTO>> LIST_OF_CUSTOMERS = new TypeReference<List<ValidCustomerDTO>>() {};
    private static final int CUSTOMER_SEED = 1;*/

}
