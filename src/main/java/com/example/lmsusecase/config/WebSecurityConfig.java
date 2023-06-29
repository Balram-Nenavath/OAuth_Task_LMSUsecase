package com.example.lmsusecase.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/oauth/token/**").permitAll()
				.antMatchers("/employees/getAll").access("hasRole('ADMIN')")
				.anyRequest().authenticated().and()
				.formLogin().permitAll().and().logout().permitAll();
		http.csrf().disable();

//		http
//				.authorizeRequests()
//				.antMatchers("/oauth/token/**").permitAll()
//				.antMatchers("/employees/getAll").hasRole("ADMIN")
//				.antMatchers("/user").hasRole("USER")
//				.anyRequest().authenticated()
//				.and()
//				.formLogin().permitAll();

	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.inMemoryAuthentication().withUser("balram").password("{noop}balram1998")
				.roles("USER")
				.and()
				.withUser("admin").password("{noop}balram1998").credentialsExpired(false)
				.accountExpired(false).accountLocked(false).authorities("WRITE_PRIVILEGES", "READ_PRIVILEGES")
				.roles("ADMIN");
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
