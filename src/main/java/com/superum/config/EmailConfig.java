package com.superum.config;

import eu.goodlike.libraries.spring.gmail.GMail;
import eu.goodlike.libraries.spring.gmail.GMailSender;
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
        String username = env.getProperty("email.username");
        String password = env.getProperty("email.password");
		return new GMailSender(username, GMail.getDefaultSender(username, password));
	}

}
