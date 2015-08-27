package com.superum.v3.table;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Similar to OptimizedTableQueryService, however the method is split into multiple parts to allow better optimization
 */
@Service
public interface SplitLessonTableQueryService {

    /**
     * @param page offset, used to find which teachers should the table be calculated for
     * @param per_page amount of teachers per page
     * @param startTime epoch milliseconds from which to start checking lessons for the table
     * @param endTime epoch milliseconds at which to stop checking lessons for the table
     * @param partitionId id of the partition of the caller
     * @return lesson table data for given parameters
     */
    Table getLessonTable(int page, int per_page, long startTime, long endTime, int partitionId);

    /**
     * @return payment data for customers with certain ids
     */
    List<TableReport> customerReport(List<Integer> customerIds, int partitionId);

    /**
     * @return payment data for teachers with certain ids
     */
    List<TableReport> teacherReport(List<Integer> teacherIds, int partitionId);

    /**
     * @return combination of methods getLessonTable, customerReport and teacherReport
     */
    FullTable getLessonTableFull(int page, int per_page, long start, long end, int partitionId);

}
