package com.superum.api.customer;

import com.superum.db.customer.Customer;
import com.superum.db.generated.timestar.tables.records.CustomerLangRecord;
import env.IntegrationTestEnvironment;
import org.jooq.InsertValuesStep3;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.CUSTOMER_LANG;
import static com.superum.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FullCustomerControllerTests extends IntegrationTestEnvironment {

    @Test
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

        FullCustomer returnedCustomer = fromResponse(result);
        int customerId = returnedCustomer.getId();

        customer = customer.withId(customerId);

        assertEquals("The returned customer should be equal to original customer, except for id field; ", returnedCustomer, customer);

        FullCustomer customerFromDB = readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    @Test
    public void readingCustomerWithValidId_shouldReturnACustomer() throws Exception {
        FullCustomer insertedCustomer = insertCustomerIntoDb(defaultFullCustomer());

        int customerId = insertedCustomer.getId();

        MvcResult result = mockMvc.perform(get("/timestar/api/v2/customer/" + customerId)
                    .contentType(APPLICATION_JSON_UTF8)
                    .header("Authorization", authHeader())
                    .content(convertObjectToJsonBytes(insertedCustomer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullCustomer returnedCustomer = fromResponse(result);

        assertEquals("The read customer should be equal to original customer; ", returnedCustomer, insertedCustomer);

        FullCustomer customerFromDB = readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    // PRIVATE

    private FullCustomer fromResponse(MvcResult result) throws IOException {
        byte[] content = result.getResponse().getContentAsByteArray();
        return convertBytesToObject(content, FullCustomer.class);
    }

    private FullCustomer readCustomerFromDb(int customerId) {
        Customer customer = sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.ID.eq(customerId))
                .fetch().stream()
                .findFirst()
                .map(Customer::valueOf)
                .orElse(null);

        if (customer == null)
            return null;

        List<String> languages = sql.select(CUSTOMER_LANG.LANGUAGE_LEVEL)
                .from(CUSTOMER_LANG)
                .where(CUSTOMER_LANG.CUSTOMER_ID.eq(customerId))
                .fetch()
                .map(record -> record.getValue(CUSTOMER_LANG.LANGUAGE_LEVEL));

        return new FullCustomer(customer, languages);
    }

    private FullCustomer insertCustomerIntoDb(FullCustomer fullCustomer) {
        Customer customer = sql.insertInto(CUSTOMER)
                .set(CUSTOMER.PARTITION_ID, 1)
                .set(CUSTOMER.NAME, fullCustomer.getName())
                .set(CUSTOMER.PAYMENT_DAY, fullCustomer.getPaymentDay())
                .set(CUSTOMER.START_DATE, fullCustomer.getStartDate())
                .set(CUSTOMER.PAYMENT_VALUE, fullCustomer.getPaymentValue())
                .set(CUSTOMER.PHONE, fullCustomer.getPhone())
                .set(CUSTOMER.WEBSITE, fullCustomer.getWebsite())
                .set(CUSTOMER.PICTURE_NAME, fullCustomer.getPictureName())
                .set(CUSTOMER.COMMENT_ABOUT, fullCustomer.getComment())
                .returning(CUSTOMER.fields())
                .fetchOne()
                .map(Customer::valueOf);

        assert customer != null; //if this assert fails, database is broken/offline

        int id = customer.getId();

        InsertValuesStep3<CustomerLangRecord, Integer, Integer, String> step = sql.insertInto(CUSTOMER_LANG, CUSTOMER_LANG.PARTITION_ID, CUSTOMER_LANG.CUSTOMER_ID, CUSTOMER_LANG.LANGUAGE_LEVEL);
        for (String language : fullCustomer.getLanguages())
            step = step.values(1, id, language);

        step.execute();

        return fullCustomer.withId(id);
    }

    private FullCustomer defaultFullCustomer() {
        return FullCustomer.newRequiredBuilder()
                .withPaymentDay(1)
                .withStartDate(Date.valueOf("2015-07-21"))
                .withPaymentValue(BigDecimal.valueOf(10))
                .withName("SUPERUM")
                .withPhone("+37069900001")
                .withWebsite("http://superum.eu/")
                .withLanguages(Arrays.asList("English: C1", "English: C2"))
                .withPictureName("pic.jpg")
                .withComment("company test")
                .build();
    }

}
