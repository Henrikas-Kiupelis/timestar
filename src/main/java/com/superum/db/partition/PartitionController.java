package com.superum.db.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.superum.helper.utils.Constants.APPLICATION_JSON_UTF8;

@RestController
@RequestMapping(value = "/timestar/api")
public class PartitionController {

	@RequestMapping(value = "/partition/add", method = RequestMethod.POST, produces = APPLICATION_JSON_UTF8)
	@ResponseBody
	public Partition addPartition(@RequestBody @Valid Partition newPartition) {
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
