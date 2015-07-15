package com.superum.db.partition;

import static com.superum.utils.ControllerUtils.RETURN_CONTENT_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/timestar/api")
public class PartitionController {

	@RequestMapping(value = "/partition/add", method = RequestMethod.POST, produces = RETURN_CONTENT_TYPE)
	@ResponseBody
	public Partition addPartition(Partition newPartition) {
		return partitionService.addPartition(newPartition);
	}

	// CONSTRUCTORS

	@Autowired
	public PartitionController(PartitionService partitionService) {
		this.partitionService = partitionService;
	}

	// PRIVATE
	
	private final PartitionService partitionService;

}
