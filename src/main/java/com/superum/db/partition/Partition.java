package com.superum.db.partition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.jooq.Record;

import java.util.Objects;

import static com.superum.db.generated.timestar.Tables.PARTITIONS;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.ALWAYS)
public class Partition {

	// PUBLIC API
	
	@JsonProperty("id") 
	public int getId() {
		return id;
	}
	
	@JsonProperty("name") 
	public String getName() {
		return name;
	}

	// OBJECT OVERRIDES

	@Override
	public String toString() {
		return "Partition" + StringUtils.toString(
				"Partition ID: " + id,
				"Name: " + name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Partition))
			return false;

		Partition other = (Partition) o;

		return this.id == other.id
				&& Objects.equals(this.name, other.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	// CONSTRUCTORS

	@JsonCreator
	public Partition(@JsonProperty("id") int id, 
					 @JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
	
	public static Partition valueOf(Record partitionRecord) {
		if (partitionRecord == null)
			return null;
		
		int id = partitionRecord.getValue(PARTITIONS.ID);
		String name = partitionRecord.getValue(PARTITIONS.NAME);
		return new Partition(id, name);
	}

	// PRIVATE
	
	private final int id;
	
	@NotEmpty
	private final String name;

}
