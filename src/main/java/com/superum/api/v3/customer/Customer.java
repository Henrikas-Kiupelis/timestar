package com.superum.api.v3.customer;

import com.google.common.base.MoreObjects;
import com.superum.api.v3.customer.dto.FetchedCustomer;
import eu.goodlike.time.Time;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Customer {

    public Optional<FetchedCustomer> create() {
        java.sql.Date startDateSql = startDate == null ? null : Time.convert(startDate).toSqlDate();
        return customerRepository.create(createdAt.toEpochMilli(), updatedAt.toEpochMilli(), startDateSql,
                name, phone, website, picture, comment);
    }

    public int update(int id) {
        java.sql.Date startDateSql = startDate == null ? null : Time.convert(startDate).toSqlDate();
        return customerRepository.update(updatedAt.toEpochMilli(), startDateSql, name, phone, website,
                picture, comment, id);
    }

    // CONSTRUCTORS

    public static Customer valueOf(Instant createdAt, Instant updatedAt, LocalDate startDate, String name,
                                   String phone, String website, String picture, String comment,
                                   CustomerRepository customerRepository) {
        return new Customer(createdAt, updatedAt, startDate, name, phone, website, picture, comment, customerRepository);
    }

    private Customer(Instant createdAt, Instant updatedAt, LocalDate startDate, String name, String phone,
                     String website, String picture, String comment, CustomerRepository customerRepository) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.startDate = startDate;
        this.name = name;
        this.phone = phone;
        this.website = website;
        this.picture = picture;
        this.comment = comment;

        this.customerRepository = customerRepository;
    }

    // PRIVATE

    private final Instant createdAt;
    private final Instant updatedAt;
    private final LocalDate startDate;
    private final String name;
    private final String phone;
    private final String website;
    private final String picture;
    private final String comment;

    private final CustomerRepository customerRepository;

    // OBJECT OVERRIDES

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdAt)
                .add("updatedAt", updatedAt)
                .add("startDate", startDate)
                .add("name", name)
                .add("phone", phone)
                .add("website", website)
                .add("picture", picture)
                .add("comment", comment)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(createdAt, customer.createdAt) &&
                Objects.equals(updatedAt, customer.updatedAt) &&
                Objects.equals(startDate, customer.startDate) &&
                Objects.equals(name, customer.name) &&
                Objects.equals(phone, customer.phone) &&
                Objects.equals(website, customer.website) &&
                Objects.equals(picture, customer.picture) &&
                Objects.equals(comment, customer.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(createdAt, updatedAt, startDate, name, phone, website, picture, comment);
    }

}
