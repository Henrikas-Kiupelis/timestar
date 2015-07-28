package com.superum.db.partition;

import com.superum.exception.DatabaseException;
import org.jooq.DSLContext;
import org.jooq.exception.DataAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.db.generated.timestar.Tables.PARTITIONS;

@Repository
@Transactional
public class PartitionDAOImpl implements PartitionDAO {

	@Override
	public Partition create(Partition partition) {
		try {
			int id = partition.getId();
			String name = partition.getName();

			int createResult = sql.insertInto(PARTITIONS)
					.set(PARTITIONS.ID, id)
					.set(PARTITIONS.NAME, name)
					.execute();
			if (createResult == 0)
				throw new DatabaseException("Couldn't insert partition: " + partition);

			return new Partition(id, name);
		} catch (DataAccessException e) {
            throw new DatabaseException("Couldn't insert partition: " + partition +
                    "; please refer to the nested exception for more info.", e);
        }
	}
	
	@Override
	public Partition read(int partitionId) {
        try {
            return sql.selectFrom(PARTITIONS)
                    .where(PARTITIONS.ID.eq(partitionId))
                    .fetch().stream()
                    .findFirst()
                    .map(Partition::valueOf)
                    .orElseThrow(() -> new DatabaseException("Couldn't find partition with ID: " + partitionId));
        } catch (DataAccessException e) {
            throw new DatabaseException("An unexpected error occurred when trying to read partition for id " + partitionId, e);
        }
	}

	// CONSTRUCTORS

	@Autowired
	public PartitionDAOImpl(DSLContext sql) {
		this.sql = sql;
	}

	// PRIVATE
	
	private final DSLContext sql;

}
