package com.superum.db.partition;

import org.springframework.stereotype.Service;

@Service
public interface PartitionService {

	Partition addPartition(Partition newPartition);
	
	Partition findPartition(int partitionId);
	
}
