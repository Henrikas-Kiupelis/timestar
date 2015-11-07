package com.superum.config;

import com.superum.api.v3.table.PaddedSumField;
import com.superum.api.v3.teacher.impl.TeacherLanguagesField;
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

    @Bean
    public TeacherLanguagesField teacherLanguagesField() {
        return new TeacherLanguagesField();
    }

}
