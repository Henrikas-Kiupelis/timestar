package com.superum.api.v3.table;

import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.customer.ValidCustomerDTO;
import com.superum.api.v2.teacher.FullTeacherDTO;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.api.v2.teacher.ValidTeacherQueryService;
import com.superum.helper.TimeResolver;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static timestar_v2.Tables.GROUP_OF_STUDENTS;

@Service
public class SplitLessonTableQueryServiceImpl implements SplitLessonTableQueryService {

    @Override
    public Table getLessonTable(int page, int amount, long startTime, long endTime, int partitionId) {
        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, amount, partitionId);
        if (teachers.isEmpty())
            return Table.empty();

        List<ValidCustomerDTO> customers = customerFetcher.getAllCustomers(partitionId);
        if (customers.isEmpty())
            return Table.empty();

        List<TableField> fields = tableDataFetcher.getFieldData(teachers, startTime, endTime, partitionId);
        return new Table(teachers, customers, fields);
    }

    @Override
    public List<TableReport> customerReport(List<Integer> customerIds, int partitionId) {
        return tableReportFetcher.reportsFor(GROUP_OF_STUDENTS.CUSTOMER_ID, customerIds, partitionId,
                tableReportFetcher::fromStartDates, CustomerNotFoundException::new);
    }

    @Override
    public List<TableReport> teacherReport(List<Integer> teacherIds, int partitionId) {
        return tableReportFetcher.reportsFor(GROUP_OF_STUDENTS.TEACHER_ID, teacherIds, partitionId,
                tableReportFetcher::fromPaymentDates, TeacherNotFoundException::new);
    }

    @Override
    public FullTable getLessonTableFull(int page, int amount, long startTime, long endTime, int partitionId) {
        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, amount, partitionId);
        if (teachers.isEmpty())
            return FullTable.empty();

        List<ValidCustomerDTO> customers = customerFetcher.getAllCustomers(partitionId);
        if (customers.isEmpty())
            return FullTable.empty();

        List<TableField> fields = tableDataFetcher.getFieldData(teachers, startTime, endTime, partitionId);

        List<Integer> teacherIds = Seq.seq(teachers).map(FullTeacherDTO::getId).toList();
        Map<Integer, TimeResolver> timeResolversForTeachers =
                Seq.zip(teacherIds.stream(),
                        teachers.stream()
                                .map(FullTeacherDTO::getPaymentDay)
                                .map(TimeResolver::from))
                        .toMap(Tuple2::v1, Tuple2::v2);
        List<TableReport> teacherReports = tableReportFetcher.reportsFor(GROUP_OF_STUDENTS.TEACHER_ID,
                teacherIds, partitionId, timeResolversForTeachers);

        List<Integer> customerIds = Seq.seq(customers)
                .filter(c -> c != null)
                .map(ValidCustomerDTO::getId).toList();
        Map<Integer, TimeResolver> timeResolversForCustomers =
                Seq.zip(customerIds.stream(),
                        customers.stream()
                                .filter(c -> c != null)
                                .map(ValidCustomerDTO::getStartDate)
                                .map(TimeResolver::from))
                        .toMap(Tuple2::v1, Tuple2::v2);
        List<TableReport> customerReports = tableReportFetcher.reportsFor(GROUP_OF_STUDENTS.CUSTOMER_ID,
                customerIds, partitionId, timeResolversForCustomers);

        return new FullTable(teachers, customers, fields, teacherReports, customerReports);
    }

    // CONSTRUCTORS

    @Autowired
    public SplitLessonTableQueryServiceImpl(ValidTeacherQueryService validTeacherQueryService,
                                            CustomerFetcher customerFetcher, TableDataFetcher tableDataFetcher,
                                            TableReportFetcher tableReportFetcher) {
        this.customerFetcher = customerFetcher;
        this.tableDataFetcher = tableDataFetcher;
        this.tableReportFetcher = tableReportFetcher;
        this.validTeacherQueryService = validTeacherQueryService;
    }

    // PRIVATE

    private final ValidTeacherQueryService validTeacherQueryService;
    private final CustomerFetcher customerFetcher;
    private final TableDataFetcher tableDataFetcher;
    private final TableReportFetcher tableReportFetcher;

}
