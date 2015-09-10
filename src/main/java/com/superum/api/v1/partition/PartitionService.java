package com.superum.api.v1.partition;

import org.springframework.stereotype.Service;

@Service
public interface PartitionService {

	Partition addPartition(Partition newPartition);
	
	Partition findPartition(int partitionId);
	
}
