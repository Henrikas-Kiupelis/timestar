package com.superum.api.table;

import com.google.common.collect.Sets;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.group.ValidGroupDTO;
import com.superum.api.table.dto.*;
import com.superum.api.teacher.FullTeacherDTO;
import com.superum.api.teacher.ValidTeacherQueryService;
import com.superum.helper.time.TimeResolver;
import eu.goodlike.time.JodaTimeZoneHandler;
import org.joda.time.LocalDate;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_1;
import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_2;
import static com.superum.db.generated.timestar.Tables.*;
import static eu.goodlike.misc.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static java.math.BigDecimal.ZERO;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.sum;

@Service
public class OptimizedLessonTableServiceImpl implements OptimizedLessonTableService {

    @Override
    public OptimizedLessonTableDTO getLessonTable(int page, int amount, long startTime, long endTime, int partitionId) {
        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, amount, partitionId);
        if (teachers.isEmpty())
            // exiting early to avoid unnecessary DB queries
            return OptimizedLessonTableDTO.empty();

        List<ValidCustomerDTO> customers = allCustomers(partitionId);
        List<TeacherLessonDataDTO> tableData = getTableData(teachers, startTime, endTime, partitionId);

        /*
         * The table data which we get from the DB is linear, meaning that it contains no empty data; however,
         * the lesson table has 2 dimensions and can certainly have empty fields (i.e. when lessons are 0)
         *
         * To simplify upcoming queries (essentially reducing some to a grouping statement), the data which was omitted
         * is created in the form of "empty" TeacherLessonDataDTO objects
         *
         * This is not a very efficient solution (quite some network overhead when parsing to JSON) but a simple one;
         * consider changing it if network performance will become a critical concern
         */
        Set<Integer> customerIds = Seq.seq(customers).map(c -> c == null ? 0 : c.getId()).toSet();
        Set<Integer> teacherIds = Seq.seq(teachers).map(FullTeacherDTO::getId).toSet();
        ensureDataUsage(customerIds, teacherIds, tableData);

        List<CustomerLessonDataDTO> customerLessonData = getCustomerLessonData(Seq.seq(customers).filter(c -> c != null).toList(),
                customerIds, tableData, partitionId);
        List<TotalLessonDataDTO> totalLessonData = getTotalLessonDataTeachers(tableData);

        /*
         * Consider creating an additional boolean parameter; this parameter by default would remove this call, unless
         * the request specifically enabled it; this will likely be the biggest performance hog in the entire request
         */
        List<PaymentDataDTO> paymentData = getPaymentDataTeachers(teachers, partitionId);

        return new OptimizedLessonTableDTO(teachers, customerLessonData, totalLessonData, paymentData);
    }

    // CONSTRUCTORS

    @Autowired
    public OptimizedLessonTableServiceImpl(DSLContext sql, ValidTeacherQueryService validTeacherQueryService) {
        this.sql = sql;
        this.validTeacherQueryService = validTeacherQueryService;
    }

    // PRIVATE

    private final DSLContext sql;
    private final ValidTeacherQueryService validTeacherQueryService;

    private List<CustomerLessonDataDTO> getCustomerLessonData(List<ValidCustomerDTO> customers, Set<Integer> customerIds, List<TeacherLessonDataDTO> tableData, int partitionId) {
        Map<Integer, List<ValidGroupDTO>> allGroups = allGroups(customerIds, partitionId);
        Map<Integer, List<TeacherLessonDataDTO>> teacherLessonData = getTeacherLessonData(tableData);
        Map<Integer, TotalLessonDataDTO> totalLessonData = getTotalLessonDataCustomers(teacherLessonData);

        /*
         * Consider creating an additional boolean parameter; this parameter by default would remove this call, unless
         * the request specifically enabled it; this will likely be the biggest performance hog in the entire request
         */
        Map<Integer, PaymentDataDTO> paymentData = getPaymentDataCustomers(customers, partitionId);

        List<CustomerLessonDataDTO> customerData = new ArrayList<>();
        for (ValidCustomerDTO customer : customers) {
            Integer customerId = customer == null ? null : customer.getId();
            CustomerLessonDataDTO customerLessonData =
                    new CustomerLessonDataDTO(customer,
                            allGroups.get(customerId),
                            teacherLessonData.get(customerId),
                            totalLessonData.get(customerId),
                            paymentData.get(customerId));
            customerData.add(customerLessonData);
        }
        return customerData;
    }

    private List<ValidCustomerDTO> allCustomers(int partitionId) {
        List<ValidCustomerDTO> allCustomers =  sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                .orderBy(CUSTOMER.ID)
                .fetch()
                .map(ValidCustomerDTO::valueOf);

        if (sql.fetchExists(GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.CUSTOMER_ID.isNull()))
            // "null" in this case means that there exist groups without a customer; they must be considered
            allCustomers.add(null);

        return allCustomers;
    }

    private Map<Integer, List<ValidGroupDTO>> allGroups(Set<Integer> customerIds, int partitionId) {
        List<ValidGroupDTO> allGroups = sql.selectFrom(GROUP_OF_STUDENTS)
                .where(GROUP_OF_STUDENTS.PARTITION_ID.eq(partitionId))
                .orderBy(GROUP_OF_STUDENTS.CUSTOMER_ID)
                .fetch()
                .map(ValidGroupDTO::valueOf);

        Map<Integer, List<ValidGroupDTO>> groupsByCustomerId = Seq.seq(allGroups)
                .map(group -> group.getCustomerId() == null ? group.withCustomerId(0) : group)
                .groupBy(ValidGroupDTO::getCustomerId);
        /*
         * This ensures that if the customer does not have groups,
         * the map will return an empty list rather than null (which might throw NullPointerException)
         */
        ensureIdUsage(customerIds, groupsByCustomerId);
        return groupsByCustomerId;
    }

    private Map<Integer, List<TeacherLessonDataDTO>> getTeacherLessonData(List<TeacherLessonDataDTO> tableData) {
        return Seq.seq(tableData).groupBy(TeacherLessonDataDTO::getCustomerId);
    }

    private Map<Integer, TotalLessonDataDTO> getTotalLessonDataCustomers(Map<Integer, List<TeacherLessonDataDTO>> teacherLessonData) {
        return Seq.seq(teacherLessonData)
                .map(t2 -> t2.map2(this::toTotalData))
                .toMap(Tuple2::v1, Tuple2::v2);
    }

    private TotalLessonDataDTO toTotalData(List<TeacherLessonDataDTO> teacherLessonDataDTOs) {
        int count = teacherLessonDataDTOs.stream()
                .mapToInt(data -> data.getLessonIds().size())
                .reduce(0, Integer::sum);
        int duration = teacherLessonDataDTOs.stream()
                .mapToInt(TeacherLessonDataDTO::getDuration)
                .reduce(0, Integer::sum);
        BigDecimal cost = teacherLessonDataDTOs.stream()
                .map(TeacherLessonDataDTO::getCost)
                .reduce(ZERO, BigDecimal::add);
        return new TotalLessonDataDTO(count, duration, cost);
    }

    private List<TotalLessonDataDTO> getTotalLessonDataTeachers(List<TeacherLessonDataDTO> tableData) {
        return Seq.seq(Seq.seq(tableData).groupBy(TeacherLessonDataDTO::getTeacherId))
                // sorts the data by teacherId, which can't be null and ensures resulting list will be correct
                .sorted((lessonData1, lessonData2) -> Integer.compare(lessonData1.v1, lessonData2.v1))
                .map(Tuple2::v2)
                .map(this::toTotalData)
                .toList();
    }

    private Table<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> nestedWageTable(Condition condition) {
        SelectHavingStep<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> union1 =
                sql.select(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID,
                        groupConcat(LESSON.ID).as(ID_FIELD),
                        sum(LESSON.DURATION_IN_MINUTES).as(DURATION_FIELD),
                        TEACHER.ACADEMIC_WAGE.mul(sum(LESSON.DURATION_IN_MINUTES)).div(45).as(COST_FIELD))
                        .from(LESSON)
                        .join(TEACHER).onKey(LESSON_IBFK_1)
                        .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                        .where(condition.andNot(GROUP_OF_STUDENTS.USE_HOURLY_WAGE))
                        .groupBy(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID);

        SelectHavingStep<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> union2 =
                sql.select(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID,
                        groupConcat(LESSON.ID).as(ID_FIELD),
                        sum(LESSON.DURATION_IN_MINUTES).as(DURATION_FIELD),
                        TEACHER.HOURLY_WAGE.mul(sum(LESSON.DURATION_IN_MINUTES)).div(60).as(COST_FIELD))
                        .from(LESSON)
                        .join(TEACHER).onKey(LESSON_IBFK_1)
                        .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                        .where(condition.and(GROUP_OF_STUDENTS.USE_HOURLY_WAGE))
                        .groupBy(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID);

        return union1.unionAll(union2).asTable("nested");
    }

    private Condition timeCondition(long start, long end, int partitionId) {
        return LESSON.PARTITION_ID.eq(partitionId).and(LESSON.TIME_OF_START.between(start, end));
    }

    private Condition teacherIdCondition(List<FullTeacherDTO> teachers, Condition condition) {
        return Seq.seq(teachers)
                .map(FullTeacherDTO::getId)
                .map(GROUP_OF_STUDENTS.TEACHER_ID::eq)
                .reduce(Condition::or)
                .map(condition::and)
                .orElse(DSL.falseCondition());
    }

    private List<TeacherLessonDataDTO> getTableData(List<FullTeacherDTO> teachers, long startTime, long endTime, int partitionId) {
        Condition timeCondition = timeCondition(startTime, endTime, partitionId);

        Condition condition = teacherIdCondition(teachers, timeCondition);

        Table<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> nested = nestedWageTable(condition);

        return sql.selectFrom(nested)
                .fetch().stream()
                .map(this::toData)
                .filter(data -> data != null)
                .collect(Collectors.toList());
    }

    private TeacherLessonDataDTO toData(Record record) {
        if (record == null)
            return null;

        Integer teacherId = record.getValue(GROUP_OF_STUDENTS.TEACHER_ID);
        if (teacherId == null)
            /*
              * This is special case scenario; when no data exists in one of the union tables, a record full of "null"
              * is returned; teacherId is the only field that cannot be null under normal circumstances
              */
            return null;

        Integer customerId = record.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID);
        if (customerId == null)
            customerId = 0;

        String ids = (String) record.getValue(ID_FIELD);
        List<Long> lessonIds = ids == null
                ? Collections.emptyList()
                : Stream.of((ids).split(MYSQL_GROUP_CONCAT_SEPARATOR)).map(Long::parseLong).collect(Collectors.toList());

        BigDecimal durationSum = (BigDecimal) record.getValue(DURATION_FIELD);
        int duration = durationSum == null ? 0 : durationSum.intValueExact();

        BigDecimal nullableCost = (BigDecimal) record.getValue(COST_FIELD);
        BigDecimal cost = nullableCost == null ? ZERO : nullableCost;

        return new TeacherLessonDataDTO(customerId, teacherId, lessonIds, duration, cost);
    }

    private List<PaymentDataDTO> getPaymentDataTeachers(List<FullTeacherDTO> teachers, int partitionId) {
        return Seq.seq(teachers).map(teacher -> paymentDataTeacher(teacher, partitionId)).toList();
    }

    private PaymentDataDTO paymentDataTeacher(FullTeacherDTO teacher, int partitionId) {
        TimeResolver timeResolver = TimeResolver.from(teacher.getPaymentDay());
        Condition condition = GROUP_OF_STUDENTS.TEACHER_ID.eq(teacher.getId())
                .and(LESSON.PARTITION_ID.eq(partitionId))
                .and(timeResolver.isBetween(LESSON.TIME_OF_START));

        Table<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> nested = nestedWageTable(condition);

        @SuppressWarnings("unchecked")
        Field<BigDecimal> sumField = sum((Field<BigDecimal>) nested.field(COST_FIELD));

        BigDecimal cost = sql.select(sumField.as("totalCost"))
                .from(nested)
                .fetch().stream()
                .map(record -> (BigDecimal) record.getValue("totalCost"))
                .filter(value -> value != null)
                .findAny()
                .orElse(ZERO);

        LocalDate endDate = JodaTimeZoneHandler.getDefault().from(timeResolver.getEndTime()).toOrgJodaTimeLocalDate().minusDays(1);
        return new PaymentDataDTO(endDate, cost);
    }

    private Map<Integer, PaymentDataDTO> getPaymentDataCustomers(List<ValidCustomerDTO> customers, int partitionId) {
        return Seq.seq(customers)
                .map(customer -> Tuple.tuple(customer.getId(), paymentDataCustomer(customer, partitionId)))
                .toMap(Tuple2::v1, Tuple2::v2);
    }

    private PaymentDataDTO paymentDataCustomer(ValidCustomerDTO customer, int partitionId) {
        if (customer == null)
            return null;

        TimeResolver timeResolver = TimeResolver.from(customer.getStartDate());
        Condition condition = GROUP_OF_STUDENTS.CUSTOMER_ID.eq(customer.getId())
                .and(LESSON.PARTITION_ID.eq(partitionId))
                .and(timeResolver.isBetween(LESSON.TIME_OF_START));

        Table<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> nested = nestedWageTable(condition);

        @SuppressWarnings("unchecked")
        Field<BigDecimal> sumField = sum((Field<BigDecimal>) nested.field(COST_FIELD));

        BigDecimal cost = sql.select(sumField.as("totalCost"))
                .from(nested)
                .fetch().stream().findAny()
                .map(record -> (BigDecimal) record.getValue("totalCost"))
                .orElse(ZERO);

        LocalDate endDate = JodaTimeZoneHandler.getDefault().from(timeResolver.getEndTime()).toOrgJodaTimeLocalDate().minusDays(1);
        return new PaymentDataDTO(endDate, cost);
    }

    private <T> void ensureIdUsage(Set<Integer> ids, Map<Integer, List<T>> partialMap) {
        for (Integer customerId : Sets.difference(ids, partialMap.keySet()))
            partialMap.put(customerId, Collections.emptyList());
    }

    private void ensureDataUsage(Set<Integer> customerIds, Set<Integer> teacherIds, List<TeacherLessonDataDTO> tableData) {
        @SuppressWarnings("unchecked")
        Set<List<Integer>> allIdCombinations = Sets.cartesianProduct(customerIds, teacherIds);

        Set<Integer> usedCustomerIds = Seq.seq(tableData).map(TeacherLessonDataDTO::getCustomerId).toSet();
        Set<Integer> usedTeacherIds = Seq.seq(tableData).map(TeacherLessonDataDTO::getTeacherId).toSet();

        @SuppressWarnings("unchecked")
        Set<List<Integer>> allUsedCombinations = Sets.cartesianProduct(usedCustomerIds, usedTeacherIds);

        List<TeacherLessonDataDTO> missingData = Seq.seq(Sets.difference(allIdCombinations, allUsedCombinations))
                .map(ids -> new TeacherLessonDataDTO(ids.get(0), ids.get(1), Collections.emptyList(), 0, ZERO))
                .toList();
        tableData.addAll(missingData);
    }

    private static final String ID_FIELD = "lessonIds";
    private static final String DURATION_FIELD = "duration";
    private static final String COST_FIELD = "cost";

}
