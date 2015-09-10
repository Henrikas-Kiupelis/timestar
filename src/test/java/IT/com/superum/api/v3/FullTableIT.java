package IT.com.superum.api.v3;

import IT.com.superum.helper.IntegrationTestEnvironment;
import com.superum.api.v2.customer.ValidCustomerDTO;
import com.superum.api.v2.lesson.ValidLessonDTO;
import com.superum.api.v2.teacher.FullTeacherDTO;
import com.superum.api.v3.table.FullTable;
import com.superum.api.v3.table.TableField;
import com.superum.api.v3.table.TableReport;
import com.superum.helper.Fakes;
import com.superum.helper.time.TimeResolver;
import eu.goodlike.functional.some.Some;
import eu.goodlike.libraries.jodatime.Time;
import eu.goodlike.libraries.mockmvc.MVC;
import eu.goodlike.test.Fake;
import org.jooq.lambda.Seq;
import org.jooq.lambda.Unchecked;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static eu.goodlike.libraries.mockmvc.HttpResult.OK;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;

@Transactional
@TransactionConfiguration(defaultRollback = true)
public class FullTableIT extends IntegrationTestEnvironment {

    @Test
    public void readingFullTable_shouldReturnTable() throws Exception {
        FullTable table = mvc.performGet(DEFAULT_PATH + DEFAULT_PARAMS, OK)
                .map(Unchecked.function(this::readTable))
                .orElseThrow(() -> new AssertionError("Should return empty table instead of null"));

        assertEquals("Returned table should be equal to the pre-computed one", precomputedTable(), table);
    }

    // PRIVATE

    private FullTable readTable(MvcResult result) throws IOException {
        return MVC.from(result).to(FullTable.class);
    }

    private FullTable precomputedTable() {
        List<FullTeacherDTO> teachers = precomputedTeachers();
        List<ValidCustomerDTO> customers = precomputedCustomers();
        List<TableField> fields = precomputedFields();
        List<TableReport> teacherReports = precomputedTeacherReports(teachers);
        List<TableReport> customerReports = precomputedCustomerReports(customers);
        return new FullTable(teachers, customers, fields, teacherReports, customerReports);
    }

    private List<FullTeacherDTO> precomputedTeachers() {
        return Some.of(Fakes::teacher).fetch(2);
    }

    private List<ValidCustomerDTO> precomputedCustomers() {
        return Some.of(Fakes::customer).fetch(2);
    }

    private List<TableField> precomputedFields() {
        return Some.ofLong(Fakes::lesson).fetchStream(2)
                .map(lesson -> new TableField(
                        lesson.getId().intValue(),
                        lesson.getId().intValue(),
                        Collections.singletonList(lesson.getId()),
                        lesson.getLength(),
                        precomputedCost(lesson)))
                .collect(Collectors.toList());
    }

    private BigDecimal precomputedCost(ValidLessonDTO lesson) {
        boolean usesHourlyWage = Fake.Boolean(lesson.getId());
        return Fake.wage(lesson.getId()).multiply(BigDecimal.valueOf(lesson.getLength()))
                .divide(BigDecimal.valueOf(usesHourlyWage ? 60 : 45), BigDecimal.ROUND_HALF_EVEN);
    }

    private List<TableReport> precomputedTeacherReports(List<FullTeacherDTO> teachers) {
        return Seq.zip(teachers.stream(),
                teachers.stream()
                        .map(FullTeacherDTO::getPaymentDay)
                        .map(TimeResolver::from)
                        .map(TimeResolver::getEndTime)
                        .map(millis -> Time.convert(millis).toJodaLocalDate().minusDays(1)))
                .map(t2 -> new TableReport(t2.v1.getId(), t2.v2, ZERO))
                .toList();
    }

    private List<TableReport> precomputedCustomerReports(List<ValidCustomerDTO> customers) {
        return Seq.zip(customers.stream(),
                customers.stream()
                        .map(ValidCustomerDTO::getStartDate)
                        .map(TimeResolver::from)
                        .map(TimeResolver::getEndTime)
                        .map(millis -> Time.convert(millis).toJodaLocalDate().minusDays(1)))
                .map(t2 -> new TableReport(t2.v1.getId(), t2.v2, ZERO))
                .toList();
    }

    private static final String DEFAULT_PATH = "/timestar/api/v3/lesson/table/data/full/";
    private static final String DEFAULT_PARAMS = "?start=" + 0 + "&end=" + Long.MAX_VALUE;

}
