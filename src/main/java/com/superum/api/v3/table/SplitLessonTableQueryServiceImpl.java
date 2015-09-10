package com.superum.api.v3.table;

import com.google.common.collect.Sets;
import com.superum.api.v2.customer.CustomerNotFoundException;
import com.superum.api.v2.customer.ValidCustomerDTO;
import com.superum.api.v2.teacher.FullTeacherDTO;
import com.superum.api.v2.teacher.TeacherNotFoundException;
import com.superum.api.v2.teacher.ValidTeacherQueryService;
import com.superum.helper.time.TimeResolver;
import eu.goodlike.libraries.jodatime.Time;
import org.joda.time.LocalDate;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.lambda.Seq;
import org.jooq.lambda.tuple.Tuple2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static eu.goodlike.misc.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static java.math.BigDecimal.ZERO;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.sum;
import static timestar_v2.Keys.LESSON_IBFK_1;
import static timestar_v2.Keys.LESSON_IBFK_2;
import static timestar_v2.Tables.*;

@Service
public class SplitLessonTableQueryServiceImpl implements SplitLessonTableQueryService {

    @Override
    public Table getLessonTable(int page, int amount, long startTime, long endTime, int partitionId) {
        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, amount, partitionId);
        if (teachers.isEmpty())
            return Table.empty();

        List<ValidCustomerDTO> customers = getAllCustomers(partitionId);
        if (customers.isEmpty())
            return Table.empty();

        List<TableField> fields = getFieldData(teachers, startTime, endTime, partitionId);
        return new Table(teachers, customers, fields);
    }

    @Override
    public List<TableReport> customerReport(List<Integer> customerIds, int partitionId) {
        return reportsFor(GROUP_OF_STUDENTS.CUSTOMER_ID, customerIds, partitionId, this::fromStartDates,
                CustomerNotFoundException::new);
    }

    @Override
    public List<TableReport> teacherReport(List<Integer> teacherIds, int partitionId) {
        return reportsFor(GROUP_OF_STUDENTS.TEACHER_ID, teacherIds, partitionId, this::fromPaymentDates,
                TeacherNotFoundException::new);
    }

    @Override
    public FullTable getLessonTableFull(int page, int amount, long startTime, long endTime, int partitionId) {
        List<FullTeacherDTO> teachers = validTeacherQueryService.readAll(page, amount, partitionId);
        if (teachers.isEmpty())
            return FullTable.empty();

        List<ValidCustomerDTO> customers = getAllCustomers(partitionId);
        if (customers.isEmpty())
            return FullTable.empty();

        List<TableField> fields = getFieldData(teachers, startTime, endTime, partitionId);

        List<Integer> teacherIds = Seq.seq(teachers).map(FullTeacherDTO::getId).toList();
        Map<Integer, TimeResolver> timeResolversForTeachers =
                Seq.zip(teacherIds.stream(),
                        teachers.stream()
                                .map(FullTeacherDTO::getPaymentDay)
                                .map(TimeResolver::from))
                        .toMap(Tuple2::v1, Tuple2::v2);
        List<TableReport> teacherReports = reportsFor(GROUP_OF_STUDENTS.TEACHER_ID, teacherIds, partitionId, timeResolversForTeachers);

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
        List<TableReport> customerReports = reportsFor(GROUP_OF_STUDENTS.CUSTOMER_ID, customerIds, partitionId, timeResolversForCustomers);

        return new FullTable(teachers, customers, fields, teacherReports, customerReports);
    }

    // CONSTRUCTORS

    @Autowired
    public SplitLessonTableQueryServiceImpl(DSLContext sql, ValidTeacherQueryService validTeacherQueryService) {
        this.sql = sql;
        this.validTeacherQueryService = validTeacherQueryService;
    }

    // PRIVATE

    private final DSLContext sql;
    private final ValidTeacherQueryService validTeacherQueryService;

    /**
     * @return list of all customers; unlike the normal method call, there is no limit in this case; also, null
     * customer is attached to the tail of this list if and only if there are groups which do not have a customer
     */
    private List<ValidCustomerDTO> getAllCustomers(int partitionId) {
        List<ValidCustomerDTO> customers =  sql.selectFrom(CUSTOMER)
                .where(CUSTOMER.PARTITION_ID.eq(partitionId))
                .orderBy(CUSTOMER.ID)
                .fetch()
                .map(ValidCustomerDTO::valueOf);

        if (sql.fetchExists(GROUP_OF_STUDENTS, GROUP_OF_STUDENTS.CUSTOMER_ID.isNull()))
            // "null" in this case means that there exist groups without a customer; they must be considered
            customers.add(null);

        return customers;
    }

    /**
     * @return the field data (customerId, teacherId, lesson ids for both of them, the duration of those lessons
     * and cost of those lessons); the cost must consider whether the group uses teacher's hourly or academic wage
     */
    private List<TableField> getFieldData(List<FullTeacherDTO> teachers, long start, long end, int partitionId) {
        return select(fullCondition(teachers, start, end, partitionId))
                .fetch()
                .map(this::toField);
    }

    /**
     * @return an SQL condition which gives: lessons in the correct partition; lessons that started between start and
     * end variables; only lessons that are assigned to the given list of teachers
     * @throws AssertionError if teacher's list is empty; this method should NEVER be called if there are no
     * teachers, because in that scenario the program can exit early.
     */
    private Condition fullCondition(List<FullTeacherDTO> teachers, long start, long end, int partitionId) {
        Condition partitionCondition = LESSON.PARTITION_ID.eq(partitionId);
        Condition timeCondition = LESSON.TIME_OF_START.between(start, end);
        Condition teacherCondition = teachers.stream()
                .map(FullTeacherDTO::getId)
                .map(GROUP_OF_STUDENTS.TEACHER_ID::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("If there were no teachers, this method would not " +
                        "be called, because the caller method would exit early."));

        return partitionCondition.and(timeCondition).and(teacherCondition);
    }

    /**
     * @return the SQL records representing a TableField; the condition is usually some form of fullCondition()
     */
    private SelectHavingStep<Record5<Integer, Integer, String, BigDecimal, BigDecimal>> select(Condition condition) {
        return sql.select(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID,
                groupConcat(LESSON.ID).as(LESSON_IDS_FIELD),
                sum(LESSON.DURATION_IN_MINUTES).as(DURATION_FIELD),
                paddedSumField())
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(GROUP_OF_STUDENTS.CUSTOMER_ID, GROUP_OF_STUDENTS.TEACHER_ID);
    }

    /**
     * @return TableField from a Record returned by select(Condition)
     */
    private TableField toField(Record record) {
        if (record == null)
            return null;

        Integer customerId = record.getValue(GROUP_OF_STUDENTS.CUSTOMER_ID);
        int teacherId = record.getValue(GROUP_OF_STUDENTS.TEACHER_ID);

        String ids = record.getValue(LESSON_IDS_FIELD, String.class);
        List<Long> lessonIds = Stream.of(ids.split(MYSQL_GROUP_CONCAT_SEPARATOR))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        BigDecimal durationSum = record.getValue(DURATION_FIELD, BigDecimal.class);
        int duration = durationSum.intValueExact();

        BigDecimal cost = record.getValue(COST_FIELD, BigDecimal.class)
                // please refer to paddedSumField() documentation for the reason of this division
                .divide(PADDING, BigDecimal.ROUND_HALF_EVEN);

        return new TableField(customerId, teacherId, lessonIds, duration, cost);
    }

    /**
     * Gets TimeResolvers using an SQL function for given ids; if the amount of resolvers is less than given ids, it
     * means that SQL query resulted in fewer rows than given ids and thus at least one of ids doesn't exist; exception
     * provider handles this scenario;
     */
    private <T extends Throwable> List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
                                         BiFunction<List<Integer>, Integer, Map<Integer, TimeResolver>> timeResolverGetterSQL,
                                         Function<String, T> exceptionProvider) throws T {
        Map<Integer, TimeResolver> timeResolvers = timeResolverGetterSQL.apply(ids, partitionId);
        if (timeResolvers.values().size() < ids.size())
            throw exceptionProvider.apply("Couldn't find these ids: " +
                    Sets.difference(Seq.seq(ids).toSet(), Sets.newHashSet(timeResolvers.keySet())));

        return reportsFor(idField, ids, partitionId, timeResolvers);
    }

    /**
     * Evaluates the amount which the teacher must be paid/customer must pay and the deadline of this payment
     */
    private List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
                                         Map<Integer, TimeResolver> timeResolvers) {
        Condition condition = forReport(idField, timeResolvers)
                .and(LESSON.PARTITION_ID.eq(partitionId));
        List<TableReport> reports = new ArrayList<>();
        for (Record record : report(idField, condition).fetch()) {
            int id = record.getValue(ID_FIELD, Integer.class);
            BigDecimal cost = record.getValue(COST_FIELD, BigDecimal.class)
                    // please refer to paddedSumField() documentation for the reason of this division
                    .divide(PADDING, BigDecimal.ROUND_HALF_EVEN);
            LocalDate endDate = Time.convert(timeResolvers.get(id).getEndTime()).toJodaLocalDate()
                    // endTime is inclusive, which means it points to the next day at 00:00:00
                    .minusDays(1);
            reports.add(new TableReport(id, endDate, cost));
        }
        // since we have the deadline even in the scenario where the amount to pay is 0, we add it to this list
        for (int id : Sets.difference(Seq.seq(ids).toSet(), Seq.seq(reports).map(TableReport::getId).toSet())) {
            LocalDate endDate = Time.convert(timeResolvers.get(id).getEndTime()).toJodaLocalDate()
                    // endTime is inclusive, which means it points to the next day at 00:00:00
                    .minusDays(1);
            reports.add(new TableReport(id, endDate, ZERO));
        }
        return reports;
    }

    /**
     * SQL function to retrieve the deadline for customers using their id
     */
    private Map<Integer, TimeResolver> fromStartDates(List<Integer> customerIds, int partitionId) {
        return sql.select(CUSTOMER.ID, CUSTOMER.START_DATE)
                .from(CUSTOMER)
                .where(forField(CUSTOMER.ID, customerIds)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .fetch().stream()
                .collect(Collectors.toMap(r -> r.getValue(CUSTOMER.ID),
                        r -> TimeResolver.from(Time.convert(r.getValue(CUSTOMER.START_DATE)).toJodaLocalDate())));
    }

    /**
     * SQL function to retrieve the deadline for teachers using their id
     */
    private Map<Integer, TimeResolver> fromPaymentDates(List<Integer> teacherIds, int partitionId) {
        return sql.select(TEACHER.ID, TEACHER.PAYMENT_DAY)
                .from(TEACHER)
                .where(forField(TEACHER.ID, teacherIds)
                        .and(TEACHER.PARTITION_ID.eq(partitionId)))
                .fetch().stream()
                .collect(Collectors.toMap(r -> r.getValue(TEACHER.ID),
                        r -> TimeResolver.from(r.getValue(TEACHER.PAYMENT_DAY))));
    }

    /**
     * @return an SQL condition which checks if a field is equal to any of given ids
     * @throws AssertionError if id list is empty; this should be filtered out at a much higher level than this
     */
    private Condition forField(Field<Integer> idField, List<Integer> ids) {
        return ids.stream()
                .map(idField::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("Empty list of ids should fail at the controller level"));
    }

    /**
     * @return an SQL condition which is used by reports; it enforces unique time constraints for every id, because
     * the deadline (and thus the period which is to be paid for) varies for every customer/teacher
     * @throws AssertionError if id list is empty; this should be filtered out at a much higher level than this
     */
    private Condition forReport(Field<Integer> idField, Map<Integer, TimeResolver> timeResolvers) {
        return Seq.seq(timeResolvers)
                .map(t2 -> idField.eq(t2.v1).and(t2.v2.isBetween(LESSON.TIME_OF_START)))
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("Empty list of ids should fail at the controller level"));
    }

    /**
     * @return the SQL records representing a TableReport; the condition is usually some form of forReport()
     */
    private SelectHavingStep<Record2<Integer, BigDecimal>> report(Field<Integer> idField, Condition condition) {
        return sql.select(idField.as(ID_FIELD), paddedSumField())
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(idField);
    }

    /**
     * <pre>
     * Explanation:
     *  if hourly wage is used, let's say X euros per hour, then the amount that must be paid for is
     *      cost = (T / 60) * X, where T is the amount of minutes the lesson lasted;
     *  if academic wage is used, let's say Y euros per academic hour, then the amount that must be paid for is
     *      cost = (T / 45) * Y, where T is the amount of minutes the lesson lasted;
     *  also, the table field will evaluate a sum of these costs:
     *      SUM for all i : cost(i); cost(i) = (Ti / [45|60]) * [Y|X]
     *  normally this would be fine, but we are using BigDecimal fields, which have limited accuracy (in our case,
     *  4 digits after the comma); also, notice that:
     *      45 = 3 * 3 * 5
     *      60 = 2 * 2 * 3 * 5
     *  both numbers have a prime factor of 3, which will result in inaccurate divisions when Ti is not divisible by 3
     *  (i.e. 50 / 3 = 16 + 2/3) and thus lose accuracy; the best way to avoid this is to do the division after all
     *  other operations (i.e. SUM) have taken place, because then the accuracy will only have a single chance to be
     *  lost; this proves to be difficult to achieve in our situation, because the amount that we need to divide by
     *  is conditional, based on which wage is used; the problem, however, can be solved by using LCM (least common
     *  multiple) of the divisors 45 and 60; from the previous factoring we can see that GCD(45, 60) is 3 * 5 = 15;
     *  then LCM(45, 60) = 60 * 45 / 15 = 60 * 3 = 180; using this we can evaluate
     *      cost * 180 = T * X / 60 * 180 = T * X * 3
     *      cost * 180 = T * Y / 45 * 180 = T * Y * 4
     *  then
     *      SUM for all i : cost(i) ->
     *          180 * SUM for all i : 180 * cost(i) ->
     *              180 * SUM for all i : Ti * [3X|4Y]
     *  this formula allows us to evaluate the sum times an integer without using any division for every cost(i), and
     *  then divide the resulting "padded" sum by said integer to get the actual sum we are looking for;
     *  while we could do the division operation directly in the SQL statement, the rounding mechanism used by SQL is
     *  a little too simplistic; I prefer using ROUND_HALF_EVEN, which supposedly minimizes the error;
     *  good enough for me!
     *  </pre>
     * @return SQL field, to be used in a SELECT statement; it pads the resulting cost like this:
     *  1) if hourly wage is used, then the resulting cost is multiplied by 3;
     *  2) if academic wage is used, then the resulting cost is multiplied by 4;
     */
    private Field<BigDecimal> paddedSumField() {
        return sum(DSL.when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE),
                TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(3))
                .when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE).not(),
                        TEACHER.ACADEMIC_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(4)))
                .as(COST_FIELD);
    }

    // LCM(45, 60) = 60 * 45 / GCD(45, 60) = 60 * 45 / 15 = 60 * 3 = 180
    private static final int PADDING_INT = 180;
    private static final BigDecimal PADDING = BigDecimal.valueOf(PADDING_INT);

    private static final String ID_FIELD = "id";
    private static final String LESSON_IDS_FIELD = "lessonIds";
    private static final String DURATION_FIELD = "duration";
    private static final String COST_FIELD = "cost * " + PADDING_INT;

}
