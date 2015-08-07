package com.superum.config;

import com.superum.helper.mail.GMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("classpath:email.properties")
@Lazy
public class EmailConfig {

	@Autowired
    Environment env;
	
	@Bean
	public GMail gmail() {
		return new GMail(env.getProperty("email.username"), env.getProperty("email.password"));
	}

}
