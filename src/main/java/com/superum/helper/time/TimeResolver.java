package com.superum.helper.time;

import eu.goodlike.libraries.jodatime.Time;
import eu.goodlike.libraries.jodatime.TimeHandler;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.jooq.Condition;
import org.jooq.TableField;

/**
 * <pre>
 * Resolves epoch milliseconds from various formats
 *
 * Intended to reduce code duplication when checking for variables, etc
 * </pre>
 */
public final class TimeResolver {

    /**
     * @return epoch milliseconds of the starting time
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @return epoch milliseconds of the ending time
     */
    public long getEndTime() {
        return endTime;
    }

    /**
     * @return condition for a field, which tests that the field's value is between the start and end epoch milliseconds
     */
    public Condition isBetween(TableField<?, Long> field) {
        return field.between(startTime, endTime);
    }

    // CONSTRUCTORS

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) if start and end are set, use those;
     * 2) if at least one of them is not set, resolve the time from remaining parameters, then use that time
     * to set start, end or both and use those;
     * </pre>
     */
    public static TimeResolver from(String timeZone, String startDate, String endDate, Long start, Long end) {
        if (start == null || end == null) {
            TimeResolver step = TimeResolver.from(timeZone, startDate, endDate);
            if (start == null)
                start = step.startTime;
            if (end == null)
                end = step.endTime;
        }
        return from(start, end);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) if timeZone is set, parse it, otherwise use default (UTC)
     * 2) if startDate is set, parse it to LocalDate using the timezone, otherwise set to current date using the timezone;
     * 3) if endDate is set, parse it to LocalDate using the timezone, otherwise set to startDate
     * 4) Resolve time from resulting DateTimeZone, start and end LocalDates
     * </pre>
     */
    public static TimeResolver from(String timeZone, String startDate, String endDate) {
        DateTimeZone dateTimeZone = timeZone == null
                ? Time.defaultTimeZone()
                : DateTimeZone.forID(timeZone);
        LocalDate localStartDate = startDate == null
                ? LocalDate.now(dateTimeZone)
                : LocalDate.parse(startDate);
        LocalDate localEndDate = endDate == null
                ? localStartDate
                : LocalDate.parse(endDate);
        return from(dateTimeZone, localStartDate, localEndDate);
    }

    /**
     * Resolves the time using JodaTimeZoneHandler for given timezone and given LocalDates
     */
    private static TimeResolver from(DateTimeZone dateTimeZone, LocalDate localStartDate, LocalDate localEndDate) {
        return from(Time.forZone(dateTimeZone), localStartDate, localEndDate);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) get start epoch milliseconds from localStartDate;
     * 2) get end epoch milliseconds from localEndDate + 1 day (so it is inclusive);
     * 3) use start and end to resolve time
     * </pre>
     */
    private static TimeResolver from(TimeHandler handler, LocalDate localStartDate, LocalDate localEndDate) {
        long start = handler.from(localStartDate).toEpochMillis();
        long end = handler.from(localEndDate.plusDays(1)).toEpochMillis();
        return from(start, end);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) get LocalDate for current time, using UTC
     * 2) find the paymentDate of this month;
     *      a) if it's after today, find the next month's paymentDate;
     *      b) if it's today or before today, find the previous month's paymentDate;
     * 3) use JodaTimeZoneHandler set to UTC, earlier payment date plus one day (so it is exclusive) and later
     * payment date to resolve time;
     * </pre>
     */
    public static TimeResolver from(int paymentDay) {
        LocalDate today = LocalDate.now(Time.defaultTimeZone());
        LocalDate dayOneOfThisMonth = today.withDayOfMonth(1);
        LocalDate paymentDate = getPaymentDate(dayOneOfThisMonth, paymentDay);

        LocalDate previousPaymentDate;
        LocalDate nextPaymentDate;
        if (today.isAfter(paymentDate)) {
            LocalDate dayOneOfNextMonth = dayOneOfThisMonth.plusMonths(1);
            previousPaymentDate = paymentDate;
            nextPaymentDate = getPaymentDate(dayOneOfNextMonth, paymentDay);
        } else {
            LocalDate dayOneOfPreviousMonth = dayOneOfThisMonth.minusMonths(1);
            previousPaymentDate = getPaymentDate(dayOneOfPreviousMonth, paymentDay);
            nextPaymentDate = paymentDate;
        }
        return from(Time.getDefault(), previousPaymentDate.plusDays(1), nextPaymentDate);
    }

    /**
     * <pre>
     * Resolves the time using the following logic:
     *
     * 1) take contract start date, add 7 days;
     * 2) take the day of the resulting date and use that to resolve time;
     * </pre>
     */
    public static TimeResolver from(LocalDate contractDate) {
        return from(contractDate.plusDays(CONTRACT_PAYMENT_DAY_DELAY).getDayOfMonth());
    }

    /**
     * @return TimeResolver for already known start/end epoch millisecond values; fairly pointless, exists for
     * compatibility only
     */
    public static TimeResolver from(long start, long end) {
        return new TimeResolver(start, end);
    }

    private TimeResolver(long startTime, long endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // PRIVATE

    private final long startTime;
    private final long endTime;

    private static final int CONTRACT_PAYMENT_DAY_DELAY = 7;

    private static LocalDate getPaymentDate(LocalDate dayOneOfAMonth, int paymentDay) {
        int maximumDay = dayOneOfAMonth.getChronology().dayOfMonth().getMaximumValue(dayOneOfAMonth);
        return maximumDay < paymentDay
                ? dayOneOfAMonth.plusMonths(1)
                : dayOneOfAMonth.withDayOfMonth(paymentDay);
    }

}
