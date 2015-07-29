package env;

import com.superum.TimeStarBackEndApplication;
import com.superum.config.PersistenceContext;
import com.superum.config.SecurityConfig;
import com.superum.helper.DatabaseHelper;
import com.superum.helper.HelperConfiguration;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;
import javax.naming.NamingException;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TimeStarBackEndApplication.class, PersistenceContext.class, SecurityConfig.class, HelperConfiguration.class})
@TransactionConfiguration(defaultRollback = true)
public abstract class IntegrationTestEnvironment {

    public static final int TEST_PARTITION = 0;

    @Before
    public void setupMockMvc() throws NamingException {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilters(this.springSecurityFilterChain)
                .build();
    }

    // PROTECTED

    protected MockMvc mockMvc;

    @Autowired
    protected DSLContext sql;

    @Autowired
    protected DatabaseHelper databaseHelper;

    protected String authHeader() {
        return TOKEN_HEADER;
    }

    protected static final Logger LOG = LoggerFactory.getLogger(IntegrationTestEnvironment.class);

    // PRIVATE

    @Autowired
    private WebApplicationContext wac;

    @Resource
    private FilterChainProxy springSecurityFilterChain;

    private static final String USERNAME = TEST_PARTITION + ".test";
    private static final String PASSWORD = "test";
    private static final byte[] TOKEN = (USERNAME + ":" + PASSWORD).getBytes();
    private static final String TOKEN_HEADER = "Basic " + Base64.encodeBase64String(TOKEN);

}
