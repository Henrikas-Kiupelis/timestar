package IT.com.superum.v3.table;

import IT.com.superum.env.IntegrationTestEnvironment;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class FullTableTestsIT extends IntegrationTestEnvironment {

    /*@Test
    public void readingFullTable_shouldReturnEmptyTable() throws Exception {
        MvcResult result = mockMvc.perform(get("/timestar/api/v3/lesson/table/data/full")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();

        FullTable fullTable = fromResponse(result, FullTable.class);

        assertEquals("Returned table should be empty", FullTable.empty(), fullTable);
    }*/

    @Test
    public void readingFullTable_shouldReturnTable() throws Exception {

    }

}
