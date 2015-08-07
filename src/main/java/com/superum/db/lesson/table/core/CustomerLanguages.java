package com.superum.db.lesson.table.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

import javax.validation.constraints.Min;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CustomerLanguages {

    // PUBLIC API

    @JsonProperty("id")
    public int getCustomerId() {
        return customerId;
    }

    @JsonProperty("languages")
    public List<String> getLanguages() {
        return languages;
    }

    // OBJECT OVERRIDES
    @Override
    public String toString() {
        return "CustomerLanguages" + StringUtils.toString(
                "Customer ID: " + customerId,
                "Languages: " + languages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof CustomerLanguages))
            return false;

        CustomerLanguages other = (CustomerLanguages) o;

        return this.customerId == other.customerId
                && Objects.equals(this.languages, other.languages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, languages);
    }

    // CONSTRUCTORS
    @JsonCreator
    public CustomerLanguages(@JsonProperty("id") int customerId,
                             @JsonProperty("languages") List<String> languages) {
        this.customerId = customerId;
        this.languages = languages != null ? languages : Collections.emptyList();
    }

    // PRIVATE

    @Min(value = 1, message = "The customer id must be set")
    private final int customerId;

    private final List<String> languages;
}
