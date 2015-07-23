package com.superum.db.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static com.superum.utils.ControllerUtils.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class PartitionController {

	@RequestMapping(value = "/partition/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
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
