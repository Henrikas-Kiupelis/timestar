package com.superum.db.customer.lang;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
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
		return StringUtils.toString(
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
		int result = 17;
		result = (result << 5) - result + customerId;
		result = (result << 5) - result + languages.hashCode();
		return result;
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
