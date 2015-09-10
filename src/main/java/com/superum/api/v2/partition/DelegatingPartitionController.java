package com.superum.api.v2.partition;

import com.superum.api.v1.partition.Partition;
import com.superum.api.v1.partition.PartitionController;
import com.superum.api.v2.exception.InvalidRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static eu.goodlike.misc.Constants.APPLICATION_JSON_UTF8;

/**
 * <pre>
 * API v2
 * Manages all requests for partitions
 * Please refer to API file for documentation of particular methods
 * (you should not be calling these methods directly unless you know what you're doing)
 *
 * Delegates the work to API v1, because the methods are not changing
 * </pre>
 */
@RestController
@RequestMapping(value = "/timestar/api/v2/partition")
public class DelegatingPartitionController {

    @RequestMapping(method = RequestMethod.PUT, consumes = APPLICATION_JSON_UTF8, produces = APPLICATION_JSON_UTF8)
    @ResponseBody
    public ValidPartitionDTO create(@RequestBody ValidPartitionDTO validPartitionDTO) {
        if (validPartitionDTO == null)
            throw new InvalidRequestException("Partition cannot be null");

        Partition partition = partitionController.addPartition(validPartitionDTO.toPartition());
        return ValidPartitionDTO.from(partition);
    }

    // CONSTRUCTORS

    @Autowired
    public DelegatingPartitionController(PartitionController partitionController) {
        this.partitionController = partitionController;
    }

    // PRIVATE

    private final PartitionController partitionController;

}
