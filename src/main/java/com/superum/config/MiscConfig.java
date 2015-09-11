package com.superum.config;

import com.superum.api.v3.table.PaddedSumField;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Lazy
public class MiscConfig {

    @Bean
    public PaddedSumField paddedSumField() {
        return new PaddedSumField();
    }

}
