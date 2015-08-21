package com.superum.api.table;

import com.superum.api.table.dto.OptimizedLessonTableDTO;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * Responsible for handling lesson table queries
 *
 * Uses CQRS model, where queries only read data, but do not have any other side effects
 * </pre>
 */
@Service
public interface OptimizedLessonTableService {

    /**
     * @param page offset, used to find which teachers should the table be calculated for
     * @param per_page amount of teachers per page
     * @param startTime epoch milliseconds from which to start checking lessons for the table
     * @param endTime epoch milliseconds at which to stop checking lessons for the table
     * @param partitionId id of the partition of the caller
     * @return lesson table for given parameters
     */
    OptimizedLessonTableDTO getLessonTable(int page, int per_page, long startTime, long endTime, int partitionId);

}
