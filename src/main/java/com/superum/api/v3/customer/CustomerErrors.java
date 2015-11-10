package com.superum.api.v3.customer;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.customer.InvalidCustomerException;

import static com.superum.api.v3.customer.CustomerConstants.COMMENT_SIZE_LIMIT;
import static eu.goodlike.misc.Constants.DEFAULT_VARCHAR_FIELD_SIZE;

public final class CustomerErrors {

    public static InvalidCustomerException startDateError(String startDate) {
        return new InvalidCustomerException("The date must be valid ISO date, not: " + startDate);
    }

    public static InvalidCustomerException nameError(String name) {
        return new InvalidCustomerException("Customer name must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + name);
    }

    public static InvalidCustomerException phoneError(String phone) {
        return new InvalidCustomerException("Customer phone must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + phone);
    }

    public static InvalidCustomerException websiteError(String website) {
        return new InvalidCustomerException("Customer website must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars or be blank: " + website);
    }

    public static InvalidCustomerException pictureError(String picture) {
        return  new InvalidCustomerException("Customer picture must not exceed " +
                DEFAULT_VARCHAR_FIELD_SIZE + " chars: " + picture);
    }

    public static InvalidCustomerException commentError(String comment) {
        return new InvalidCustomerException("Customer comment must not exceed " +
                COMMENT_SIZE_LIMIT + " chars: " + comment);
    }

    public static CustomerNotFoundException customerIdError(int id) {
        return new CustomerNotFoundException("Couldn't find customer with id: " + id);
    }

    // PRIVATE

    private CustomerErrors() {
        throw new AssertionError("Do not instantiate, use static methods!");
    }


}
