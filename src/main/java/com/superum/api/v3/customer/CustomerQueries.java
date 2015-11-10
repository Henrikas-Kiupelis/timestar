package com.superum.api.v3.customer;

import com.superum.api.v3.customer.dto.FetchedCustomer;

import java.util.List;

public interface CustomerQueries {

    FetchedCustomer readById(int id);

    List<FetchedCustomer> readForTeacher(int teacherId, int page, int amount);

    List<FetchedCustomer> readAll(int page, int amount);

    int countForTeacher(int teacherId);

    int countAll();

}
