package com.superum.api.table;

import env.IntegrationTestEnvironment;
import org.junit.Test;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static com.superum.utils.JsonUtils.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class OptimizedLessonTableControllerTests extends IntegrationTestEnvironment {

    @Test
    public void test() throws Exception {
        MvcResult result = mockMvc.perform(get("/timestar/api/v2/lesson/table")
                .contentType(APPLICATION_JSON_UTF8)
                .header("Authorization", authHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andReturn();
    }

}
