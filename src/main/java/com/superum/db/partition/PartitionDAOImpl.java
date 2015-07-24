package com.superum.db.partition;

import com.superum.db.exception.DatabaseException;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Tables.PARTITIONS;

@Repository
@Transactional
public class PartitionDAOImpl implements PartitionDAO {

	@Override
	public Partition create(Partition partition) {
		int id = partition.getId();
		String name = partition.getName();
		
		int createResult = sql.insertInto(PARTITIONS)
				.set(PARTITIONS.ID, id)
				.set(PARTITIONS.NAME, name)
				.execute();
		if (createResult == 0)
			throw new DatabaseException("Couldn't insert partition: " + partition);
		
		return new Partition(id, name);
	}
	
	@Override
	public Partition read(int partitionId) {
		return sql.selectFrom(PARTITIONS)
				.where(PARTITIONS.ID.eq(partitionId))
				.fetch().stream()
				.findFirst()
				.map(Partition::valueOf)
				.orElseThrow(() -> new DatabaseException("Couldn't find partition with ID: " + partitionId));
	}

	// CONSTRUCTORS

	@Autowired
	public PartitionDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
