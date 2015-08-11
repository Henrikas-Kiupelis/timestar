package com.superum.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.jooq.ExecuteContext;
import org.jooq.SQLDialect;
import org.jooq.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:persistence.properties")
@Lazy
public class PersistenceContext {
 
    @Autowired
    Environment env;

    @Bean
    @Primary
    @DependsOn("comboPooledDataSource")
    public DataSource dataSource() {
        return comboPooledDataSource();
    }

    @Bean(destroyMethod = "close")
    public ComboPooledDataSource comboPooledDataSource() {
    	ComboPooledDataSource dataSource = new ComboPooledDataSource();
 
        try {
			dataSource.setDriverClass(env.getRequiredProperty("db.driver"));
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Failed to load database driver.", e);
		}
        dataSource.setJdbcUrl(env.getRequiredProperty("db.url"));
        dataSource.setUser(env.getRequiredProperty("db.username"));
        dataSource.setPassword(env.getRequiredProperty("db.password"));
        return dataSource;
    }
 
    @Bean
    @DependsOn("dataSource")
    public LazyConnectionDataSourceProxy lazyConnectionDataSource() {
        return new LazyConnectionDataSourceProxy(dataSource());
    }
 
    @Bean
    @DependsOn("dataSource")
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(lazyConnectionDataSource());
    }
 
    @Bean
    @DependsOn("dataSource")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(lazyConnectionDataSource());
    }
 
    @Bean
    @DependsOn("dataSource")
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }
 
    @Bean
    @DependsOn("dataSource")
    public JOOQToSpringExceptionTransformer jooqToSpringExceptionTransformer() {
        return new JOOQToSpringExceptionTransformer();
    }
 
    @Bean
    @DependsOn("dataSource")
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();
 
        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(new DefaultExecuteListenerProvider(
            jooqToSpringExceptionTransformer()
        ));
 
        String sqlDialectName = env.getRequiredProperty("jooq.sql.dialect");
        SQLDialect dialect = SQLDialect.valueOf(sqlDialectName);
        jooqConfiguration.set(dialect);

        return jooqConfiguration;
    }
 
    @Bean
    @DependsOn("dataSource")
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }
 
    @Bean
    @DependsOn("dataSource")
    public DataSourceInitializer dataSourceInitializer() {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource());
 
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(
                new ClassPathResource(env.getRequiredProperty("db.schema.script"))
        );
 
        initializer.setDatabasePopulator(populator);
        
        return initializer;
    }
    
    static class JOOQToSpringExceptionTransformer extends DefaultExecuteListener {
    	 
		private static final long serialVersionUID = -2120575381798245613L;

		@Override
        public void exception(ExecuteContext ctx) {
            SQLDialect dialect = ctx.configuration().dialect();
            SQLExceptionTranslator translator = (dialect != null)
                    ? new SQLErrorCodeSQLExceptionTranslator(dialect.name())
                    : new SQLStateSQLExceptionTranslator();
     
            ctx.exception(translator.translate("jOOQ", ctx.sql(), ctx.sqlException()));
        }
    }
    
}
