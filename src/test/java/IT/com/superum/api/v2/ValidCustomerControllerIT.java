package IT.com.superum.api.v2;

import IT.com.superum.helper.DB;
import IT.com.superum.helper.IntegrationTestEnvironment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.superum.api.v2.customer.ValidCustomerDTO;
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
public class ValidCustomerControllerIT extends IntegrationTestEnvironment {

    @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        ValidCustomerDTO customer = Fakes.customer(NEW_CUSTOMER_ID).withoutId();

        ValidCustomerDTO insertedCustomer = mvc.performPut(DEFAULT_PATH, customer, OK)
                .map(Unchecked.function(this::readCustomer))
                .orElseThrow(() -> new Exception("Successful insertion should return a customer!"));

        assertEquals("Inserted customer should be equal to the original (except id)",
                customer.withId(insertedCustomer.getId()), insertedCustomer);

        assertInDatabase(insertedCustomer);
    }

    @Test
    public void insertingCustomerWithId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = Fakes.customer(NEW_CUSTOMER_ID);

        mvc.performPut(DEFAULT_PATH, customer, BAD, status().isBadRequest());

        assertNotInDatabase(customer);
    }

    @Test
    public void insertingCustomerWithoutName_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = ValidCustomerDTO.builder()
                .startDate(Fakes.localDate(NEW_CUSTOMER_ID))
                .phone(Fake.phone(NEW_CUSTOMER_ID))
                .website(Fake.website(NEW_CUSTOMER_ID))
                .build();

        mvc.performPut(DEFAULT_PATH, customer, BAD, status().isBadRequest());
    }

    @Test
    public void updatingCustomerWithId_shouldUpdateCustomer() throws Exception {
        ValidCustomerDTO customer = Fakes.customer(NEW_CUSTOMER_ID).withId(OLD_CUSTOMER_ID);

        mvc.performPost(DEFAULT_PATH, customer, OK_NO_BODY);

        assertInDatabase(customer);
    }

    @Test
    public void updatingCustomerWithIdAndNameOnly_shouldUpdateCustomer() throws Exception {
        ValidCustomerDTO partialCustomer = ValidCustomerDTO.builder()
                .id(OLD_CUSTOMER_ID)
                .name(Fake.name(NEW_STUDENT_ID))
                .build();

        mvc.performPost(DEFAULT_PATH, partialCustomer, OK_NO_BODY);

        ValidCustomerDTO beforeUpdate = Fakes.customer(OLD_CUSTOMER_ID);
        ValidCustomerDTO afterUpdate = ValidCustomerDTO.stepBuilder()
                .startDate(beforeUpdate.getStartDate())
                .name(partialCustomer.getName())
                .phone(beforeUpdate.getPhone())
                .website(beforeUpdate.getWebsite())
                .id(partialCustomer.getId())
                .picture(beforeUpdate.getPicture())
                .comment(beforeUpdate.getComment())
                .build();

        assertInDatabase(afterUpdate);
    }

    @Test
    public void updatingCustomerWithoutId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = Fakes.customer(NEW_CUSTOMER_ID).withoutId();

        mvc.performPost(DEFAULT_PATH, customer, BAD, status().isBadRequest());
    }

    @Test
    public void updatingCustomerWithOnlyId_shouldReturn400() throws Exception {
        ValidCustomerDTO customer = ValidCustomerDTO.builder()
                .id(OLD_CUSTOMER_ID)
                .build();

        mvc.performPost(DEFAULT_PATH, customer, BAD, status().isBadRequest());

        assertInDatabase(Fakes.customer(OLD_CUSTOMER_ID));
    }

    @Test
    public void updatingCustomerWithNonExistentId_shouldReturn404() throws Exception {
        ValidCustomerDTO customer = Fakes.customer(NEW_CUSTOMER_ID);

        mvc.performPost(DEFAULT_PATH, customer, BAD, status().isNotFound());

        assertNotInDatabase(customer);
    }

    @Test
    public void deletingCustomerById_shouldDeleteCustomer() throws Exception {
        ValidCustomerDTO customer = db.insertValidCustomer(Fakes.customer(NEW_CUSTOMER_ID));

        mvc.performDelete(DEFAULT_PATH + customer.getId(), OK_NO_BODY);

        assertNotInDatabase(customer);
    }

    @Test
    public void deletingCustomerByStillUsedId_shouldReturn400() throws Exception {
        mvc.performDelete(DEFAULT_PATH + OLD_CUSTOMER_ID, BAD, status().isBadRequest());

        assertInDatabase(OLD_CUSTOMER_ID);
    }

    @Test
    public void deletingCustomerByNonExistentId_shouldReturn404() throws Exception {
        mvc.performDelete(DEFAULT_PATH + NEW_CUSTOMER_ID, BAD, status().isNotFound());

        assertNotInDatabase(NEW_CUSTOMER_ID);
    }

    @Test
    public void readingCustomerById_shouldReturnCustomer() throws Exception {
        ValidCustomerDTO customer = mvc.performGet(DEFAULT_PATH + OLD_CUSTOMER_ID, OK)
                .map(Unchecked.function(this::readCustomer))
                .orElseThrow(() -> new Exception("Successful read should return a customer!"));

        assertInDatabase(customer);
    }

    @Test
    public void readingCustomerByNonExistentId_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + NEW_CUSTOMER_ID, BAD, status().isNotFound());
    }

    @Test
    public void readingAllCustomers_shouldReturnCustomers() throws Exception {
        List<ValidCustomerDTO> customers = mvc.performGet(DEFAULT_PATH, OK)
                .map(Unchecked.function(this::readCustomers))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning all, 2 objects should be returned", 2, customers.size());
        customers.forEach(this::assertInDatabase);
    }

    @Test
    public void countingAllCustomers_shouldReturnCountOf2() throws Exception {
        int count = mvc.performGet(DEFAULT_PATH + "count", OK)
                .map(Unchecked.function(this::readCount))
                .orElseThrow(() -> new Exception("Successful read should return an integer!"));

        assertEquals("When counting all, 2 should be returned", 2, count);
    }

    @Test
    public void readingCustomersForTeacher_shouldReturnCustomers() throws Exception {
        List<ValidCustomerDTO> customers = mvc.performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID, OK)
                .map(Unchecked.function(this::readCustomers))
                .orElseThrow(() -> new AssertionError("Should return empty list instead of null"));

        assertEquals("When returning for something, 1 object should be returned", 1, customers.size());
        customers.forEach(this::assertInDatabase);
    }

    @Test
    public void readingCustomersForNonExistentTeacher_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID, BAD, status().isNotFound());
    }

    @Test
    public void countingCustomersForTeacher_shouldReturnCountOf1() throws Exception {
        int count = mvc.performGet(DEFAULT_PATH + "teacher/" + OLD_TEACHER_ID + "/count", OK)
                .map(Unchecked.function(this::readCount))
                .orElseThrow(() -> new Exception("Successful read should return an integer!"));

        assertEquals("When counting for something, 1 should be returned", 1, count);
    }

    @Test
    public void countingCustomersForNonExistentTeacher_shouldReturn404() throws Exception {
        mvc.performGet(DEFAULT_PATH + "teacher/" + NEW_TEACHER_ID + "/count", BAD, status().isNotFound());
    }

    // PRIVATE

    private ValidCustomerDTO readCustomer(MvcResult result) throws IOException {
        return MVC.from(result).to(ValidCustomerDTO.class);
    }

    private List<ValidCustomerDTO> readCustomers(MvcResult result) throws IOException {
        return MVC.from(result).to(LIST_OF_CUSTOMERS);
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

    private void assertInDatabase(int customerId) {
        assertInDatabase(DB::readValidCustomer, customerId);
    }

    private static final String DEFAULT_PATH = "/timestar/api/v2/customer/";

    private static final TypeReference<List<ValidCustomerDTO>> LIST_OF_CUSTOMERS = new TypeReference<List<ValidCustomerDTO>>() {};

}
