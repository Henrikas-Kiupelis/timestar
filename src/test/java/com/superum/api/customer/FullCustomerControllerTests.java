package com.superum.api.customer;

import com.superum.db.customer.Customer;
import env.IntegrationTestEnvironment;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static com.superum.db.generated.timestar.Tables.CUSTOMER;
import static com.superum.db.generated.timestar.Tables.CUSTOMER_LANG;
import static com.superum.utils.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FullCustomerControllerTests extends IntegrationTestEnvironment {

    @Test
    public void insertingCustomerWithoutId_shouldCreateNewCustomer() throws Exception {
        byte paymentDay = 1;
        Date startDate = Date.valueOf("2015-07-21");
        BigDecimal paymentValue = BigDecimal.valueOf(10);
        String name = "SUPERUM";
        String phone = "+37069900001";
        String website = "http://superum.eu/";
        List<String> languages = Arrays.asList("English: C1", "English: C2");
        String pictureName = "pic.jpg";
        String comment = "company test";

        FullCustomer customer = FullCustomer.newRequiredBuilder()
                .withPaymentDay(paymentDay)
                .withStartDate(startDate)
                .withPaymentValue(paymentValue)
                .withName(name)
                .withPhone(phone)
                .withWebsite(website)
                .withLanguages(languages)
                .withPictureName(pictureName)
                .withComment(comment)
                .build();

        MvcResult result = mockMvc.perform(post("/timestar/api/v2/customer/create")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader())
                .content(convertObjectToJsonBytes(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        byte[] content = result.getResponse().getContentAsByteArray();
        FullCustomer returnedCustomer = convertBytesToObject(content, FullCustomer.class);
        int customerId = returnedCustomer.getId();

        customer = customer.withId(customerId);

        assertEquals("The returned customer should be equal to original customer, except for id field; ", returnedCustomer, customer);

        FullCustomer customerFromDB = readCustomerFromDb(customerId);

        assertEquals("The customer in the database should be equal to the returned customer; ", customerFromDB, returnedCustomer);
    }

    // PRIVATE

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

}
