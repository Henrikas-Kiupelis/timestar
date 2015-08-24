package com.superum.api.partition;

import com.superum.api.exception.InvalidRequestException;
import com.superum.db.partition.Partition;
import com.superum.db.partition.PartitionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.superum.helper.Constants.APPLICATION_JSON_UTF8;

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
    public ValidPartition addPartition(@RequestBody ValidPartition validPartition) {
        if (validPartition == null)
            throw new InvalidRequestException("Partition cannot be null");

        Partition partition = partitionController.addPartition(validPartition.toPartition());
        return ValidPartition.from(partition);
    }

    // CONSTRUCTORS

    @Autowired
    public DelegatingPartitionController(PartitionController partitionController) {
        this.partitionController = partitionController;
    }

    // PRIVATE

    private final PartitionController partitionController;

}
