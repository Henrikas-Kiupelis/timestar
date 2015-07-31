package com.superum.db.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PartitionServiceImpl implements PartitionService {

	@Override
	public Partition addPartition(Partition newPartition) {
		return partitionDAO.create(newPartition);
	}
	
	@Override
	public Partition findPartition(int partitionId) {
		return partitionDAO.read(partitionId);
	}

	// CONSTRUCTORS

	@Autowired
	public PartitionServiceImpl(PartitionDAO partitionDAO) {
		this.partitionDAO = partitionDAO;
	}

	// PRIVATE
	
	private final PartitionDAO partitionDAO;

}
