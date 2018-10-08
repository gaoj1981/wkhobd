/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.taoxeo.boot.security.AbstractWebSecurityConfig;
import com.wkhmedical.security.DefaultAuthenticationFailureHandler;
import com.wkhmedical.security.DefaultAuthenticationSuccessHandler;
import com.wkhmedical.security.DefaultUserDetailsService;

/**
 * The Class WebSecurityConfig.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Configuration
@ConditionalOnWebApplication
@EnableSpringHttpSession
@EnableRedisHttpSession
public class WebSecurityConfig extends AbstractWebSecurityConfig {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.taoxeo.boot.security.AbstractWebSecurityConfig#configure(org.springframework.security.
	 * config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		 http
		 		.authorizeRequests()
            	.antMatchers("/wx/login").permitAll()
				.antMatchers("/wx/**", "/api/**").hasAnyRole("USER", "WXUSER")
				.antMatchers("/**").hasAnyRole("USER")
				.and().formLogin().loginPage("/login.html").loginProcessingUrl("/login")
				.successHandler(new DefaultAuthenticationSuccessHandler())
				.failureHandler(new DefaultAuthenticationFailureHandler())
				.permitAll()
				.and().logout()
				.and().csrf().disable()
				.sessionManagement().enableSessionUrlRewriting(true)
				.and().exceptionHandling();
		//@formatter:on
	}

	/* (non-Javadoc)
	 * @see com.taoxeo.boot.security.AbstractWebSecurityConfig#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/device/**", "/webjars/**", "/anonymous/**", "/images/**", "/v2/**", "/swagger**", "/static/**", "/favicon.ico", "/index.html", "/swagger-resources/**");
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#userDetailsService()
	 */
	@Override
	public DefaultUserDetailsService userDetailsService() {
		return new DefaultUserDetailsService();
	}
}
