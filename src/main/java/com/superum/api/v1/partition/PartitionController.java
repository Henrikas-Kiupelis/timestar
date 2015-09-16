package com.superum.api.v1.partition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/timestar/api")
public class PartitionController {

	@RequestMapping(value = "/partition/add", method = POST, produces = APPLICATION_JSON_UTF8)
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
