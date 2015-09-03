package com.superum.env;

import com.superum.config.PersistenceContext;
import com.superum.helper.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class HelperConfiguration {

    @Bean
    public DB db() {
        return new DB(persistenceContext.dsl());
    }

    // PRIVATE

    @Autowired
    private PersistenceContext persistenceContext;

}
