package com.superum.db.partition;

import static com.superum.db.generated.timestar.Tables.PARTITIONS;

import java.util.Objects;

import org.hibernate.validator.constraints.NotEmpty;
import org.jooq.Record;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.superum.utils.StringUtils;

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
		return StringUtils.toString(
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
		int result = 17;
		result = (result << 5) - result + id;
		result = (result << 5) - result + (name == null ? 0 : name.hashCode());
		return result;
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
