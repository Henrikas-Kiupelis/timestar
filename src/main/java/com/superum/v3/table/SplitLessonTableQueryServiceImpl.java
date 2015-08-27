package com.superum.v3.table;

import com.google.common.collect.Sets;
import com.superum.api.customer.CustomerNotFoundException;
import com.superum.api.customer.ValidCustomerDTO;
import com.superum.api.teacher.FullTeacherDTO;
import com.superum.api.teacher.TeacherNotFoundException;
import com.superum.api.teacher.ValidTeacherQueryService;
import com.superum.helper.time.JodaTimeZoneHandler;
import com.superum.helper.time.TimeResolver;
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

import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_1;
import static com.superum.db.generated.timestar.Keys.LESSON_IBFK_2;
import static com.superum.db.generated.timestar.Tables.*;
import static com.superum.helper.Constants.MYSQL_GROUP_CONCAT_SEPARATOR;
import static java.math.BigDecimal.ZERO;
import static org.jooq.impl.DSL.groupConcat;
import static org.jooq.impl.DSL.sum;

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

    private List<TableField> getFieldData(List<FullTeacherDTO> teachers, long start, long end, int partitionId) {
        return select(fullCondition(teachers, start, end, partitionId))
                .fetch()
                .map(this::toField);
    }

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

        BigDecimal cost = record.getValue(COST_FIELD, BigDecimal.class).divide(PADDING, BigDecimal.ROUND_HALF_EVEN);

        return new TableField(customerId, teacherId, lessonIds, duration, cost);
    }

    private <T extends Throwable> List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
                                         BiFunction<List<Integer>, Integer, Map<Integer, TimeResolver>> timeResolverGetter,
                                         Function<String, T> exceptionProvider) throws T {
        Map<Integer, TimeResolver> timeResolvers = timeResolverGetter.apply(ids, partitionId);
        if (timeResolvers.values().size() < ids.size())
            throw exceptionProvider.apply("Couldn't find these ids: " +
                    Sets.difference(Seq.seq(ids).toSet(), Sets.newHashSet(timeResolvers.keySet())));

        return reportsFor(idField, ids, partitionId, timeResolvers);
    }

    private List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
                                         Map<Integer, TimeResolver> timeResolvers) {
        Condition condition = forReport(idField, timeResolvers)
                .and(LESSON.PARTITION_ID.eq(partitionId));
        List<TableReport> reports = new ArrayList<>();
        for (Record record : report(idField, condition).fetch()) {
            int id = record.getValue(ID_FIELD, Integer.class);
            BigDecimal cost = record.getValue(COST_FIELD, BigDecimal.class).divide(PADDING, BigDecimal.ROUND_HALF_EVEN);
            LocalDate endDate = JodaTimeZoneHandler.getDefault()
                    .from(timeResolvers.get(id).getEndTime())
                    .toOrgJodaTimeLocalDate().minusDays(1);
            reports.add(new TableReport(id, endDate, cost));
        }
        for (int id : Sets.difference(Seq.seq(ids).toSet(), Seq.seq(reports).map(TableReport::getId).toSet())) {
            LocalDate endDate = JodaTimeZoneHandler.getDefault()
                    .from(timeResolvers.get(id).getEndTime())
                    .toOrgJodaTimeLocalDate().minusDays(1);
            reports.add(new TableReport(id, endDate, ZERO));
        }
        return reports;
    }

    private Map<Integer, TimeResolver> fromStartDates(List<Integer> customerIds, int partitionId) {
        return sql.select(CUSTOMER.ID, CUSTOMER.START_DATE)
                .from(CUSTOMER)
                .where(forField(CUSTOMER.ID, customerIds)
                        .and(CUSTOMER.PARTITION_ID.eq(partitionId)))
                .fetch().stream()
                .collect(Collectors.toMap(r -> r.getValue(CUSTOMER.ID),
                        r -> TimeResolver.from(JodaTimeZoneHandler.getDefault()
                                .from(r.getValue(CUSTOMER.START_DATE))
                                .toOrgJodaTimeLocalDate())));
    }

    private Map<Integer, TimeResolver> fromPaymentDates(List<Integer> teacherIds, int partitionId) {
        return sql.select(TEACHER.ID, TEACHER.PAYMENT_DAY)
                .from(TEACHER)
                .where(forField(TEACHER.ID, teacherIds)
                        .and(TEACHER.PARTITION_ID.eq(partitionId)))
                .fetch().stream()
                .collect(Collectors.toMap(r -> r.getValue(TEACHER.ID),
                        r -> TimeResolver.from(r.getValue(TEACHER.PAYMENT_DAY))));
    }

    private Condition forField(Field<Integer> idField, List<Integer> ids) {
        return ids.stream()
                .map(idField::eq)
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("Empty list of ids should fail at the controller level"));
    }

    private Condition forReport(Field<Integer> idField, Map<Integer, TimeResolver> timeResolvers) {
        return Seq.seq(timeResolvers)
                .map(t2 -> idField.eq(t2.v1).and(t2.v2.isBetween(LESSON.TIME_OF_START)))
                .reduce(Condition::or)
                .orElseThrow(() -> new AssertionError("Empty list of ids should fail at the controller level"));
    }

    private SelectHavingStep<Record2<Integer, BigDecimal>> report(Field<Integer> idField, Condition condition) {
        return sql.select(idField.as(ID_FIELD), paddedSumField())
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(idField);
    }

    private Field<BigDecimal> paddedSumField() {
        return sum(DSL.when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE),
                TEACHER.HOURLY_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(3))
                .when(DSL.condition(GROUP_OF_STUDENTS.USE_HOURLY_WAGE).not(),
                        TEACHER.ACADEMIC_WAGE.mul(LESSON.DURATION_IN_MINUTES).mul(4)))
                .as(COST_FIELD);
    }

    private static final String ID_FIELD = "id";
    private static final String LESSON_IDS_FIELD = "lessonIds";
    private static final String DURATION_FIELD = "duration";
    private static final String COST_FIELD = "cost * 180";

    // 3 * 60 or 4 * 45
    private static final BigDecimal PADDING = BigDecimal.valueOf(180);

}
