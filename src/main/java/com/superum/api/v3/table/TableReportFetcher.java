package com.superum.api.v3.table;

import com.google.common.collect.Sets;
import com.superum.helper.TimeResolver;
import eu.goodlike.libraries.joda.time.Time;
import org.joda.time.LocalDate;
import org.jooq.*;
import org.jooq.lambda.Seq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;
import static timestar_v2.Keys.LESSON_IBFK_1;
import static timestar_v2.Keys.LESSON_IBFK_2;
import static timestar_v2.Tables.*;

@Repository
public class TableReportFetcher {

    /**
     * Gets TimeResolvers using an SQL function for given ids; if the amount of resolvers is less than given ids, it
     * means that SQL query resulted in fewer rows than given ids and thus at least one of ids doesn't exist; exception
     * provider handles this scenario;
     */
    public <T extends Throwable> List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
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
    public List<TableReport> reportsFor(Field<Integer> idField, List<Integer> ids, int partitionId,
                                         Map<Integer, TimeResolver> timeResolvers) {
        Condition condition = forReport(idField, timeResolvers)
                .and(LESSON.PARTITION_ID.eq(partitionId));
        List<TableReport> reports = new ArrayList<>();
        for (Record record : report(idField, condition).fetch()) {
            int id = record.getValue(ID_FIELD, Integer.class);
            BigDecimal cost = paddedSumField.valueForRecord(record);
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
    public Map<Integer, TimeResolver> fromStartDates(List<Integer> customerIds, int partitionId) {
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
    public Map<Integer, TimeResolver> fromPaymentDates(List<Integer> teacherIds, int partitionId) {
        return sql.select(TEACHER.ID, TEACHER.PAYMENT_DAY)
                .from(TEACHER)
                .where(forField(TEACHER.ID, teacherIds)
                        .and(TEACHER.PARTITION_ID.eq(partitionId)))
                .fetch().stream()
                .collect(Collectors.toMap(r -> r.getValue(TEACHER.ID),
                        r -> TimeResolver.from(r.getValue(TEACHER.PAYMENT_DAY))));
    }

    // CONSTRUCTORS

    @Autowired
    public TableReportFetcher(DSLContext sql, PaddedSumField paddedSumField) {
        this.sql = sql;
        this.paddedSumField = paddedSumField;
    }

    // PRIVATE

    private final DSLContext sql;
    private final PaddedSumField paddedSumField;

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
        return sql.select(idField.as(ID_FIELD), paddedSumField.field())
                .from(LESSON)
                .join(TEACHER).onKey(LESSON_IBFK_1)
                .join(GROUP_OF_STUDENTS).onKey(LESSON_IBFK_2)
                .where(condition)
                .groupBy(idField);
    }

    private static final String ID_FIELD = "id";

}
