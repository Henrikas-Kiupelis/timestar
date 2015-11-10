package com.superum.api.v3.customer;

import com.superum.api.v3.customer.dto.FetchedCustomer;

import java.util.Optional;

public interface CustomerRepository {

    Optional<FetchedCustomer> create(long createdAt, long updatedAt, java.sql.Date startDate, String name, String phone,
                                     String website, String picture, String comment);

    int update(long updatedAt, java.sql.Date startDate, String name, String phone, String website, String picture,
               String comment, int id);

    int delete(int id);

}
