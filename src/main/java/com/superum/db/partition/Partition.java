package com.superum.db.partition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.superum.helper.Equals;
import org.hibernate.validator.constraints.NotEmpty;
import org.jooq.Record;

import java.util.Arrays;
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
        return MoreObjects.toStringHelper("Partition")
                .add("Partition id", id)
                .add("Name", name)
                .toString();
	}

	@Override
	public boolean equals(Object o) {
        return this == o || o instanceof Partition && EQUALS.equals(this, (Partition) o);
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

    private static final Equals<Partition> EQUALS = new Equals<>(Arrays.asList(Partition::getId, Partition::getName));

}
