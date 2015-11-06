package com.superum.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.jdbcAuthentication()
				.dataSource(dataSource)
				.passwordEncoder(passwordEncoder())
				.usersByUsernameQuery(USERS_QUERY)
				.authoritiesByUsernameQuery(AUTHORITIES_QUERY);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.antMatchers(PERMISSION_ALL).permitAll()
				.antMatchers(HttpMethod.GET, "/**").hasRole(Role.TEACHER.name())
				.antMatchers(PERMISSION_TEACHER).hasRole(Role.TEACHER.name())
				.anyRequest().hasRole(Role.ADMIN.name())
				.and()
				.httpBasic()
				.and()
			.csrf().disable();
	}

	@Bean
	@Override
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return super.userDetailsServiceBean();
	}

	public enum Role {

		ADMIN,
		TEACHER;

		public String fullName() {
			return "ROLE_" + this.name();
		}

		public static List<String> allFullNames() {
			return Arrays.stream(Role.values())
					.map(Role::fullName)
					.collect(Collectors.toList());
		}

	}

    // PRIVATE

    private static final String USERS_QUERY = "select username,password,enabled from account where username = ?";
    private static final String AUTHORITIES_QUERY = "select username,role from roles where username = ?";

    private static final String[] PERMISSION_ALL = {
            "/timestar/api/v2/misc/**"
    };
    private static final String[] PERMISSION_TEACHER = {
            "/timestar/api/account/update",
            "/timestar/api/lesson/**"
    };


}
