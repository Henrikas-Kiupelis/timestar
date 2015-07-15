package com.superum.db.partition;

import org.springframework.stereotype.Repository;

@Repository
public interface PartitionDAO {
	
	Partition create(Partition partition);
	
	Partition read(int partitionId);

}
