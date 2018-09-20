/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import com.taoxeo.boot.security.oauth2.AbstractAuthorizationServerConfig;
import com.taoxeo.boot.security.oauth2.AbstractResourceServerConfiguration;

/**
 * The Class WebOAuthConfig.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Configuration
@ConditionalOnWebApplication
public class WebOAuthConfig {

	/** The Constant RESOURCE_ID. */
	private static final String RESOURCE_ID = "restservice";

	/**
	 * The Class AuthorizationServerConfig.
	 *
	 * @author Derek
	 * @since 1.0, 2018-9-13
	 */
	@Configuration
	@ConditionalOnWebApplication
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfig extends AbstractAuthorizationServerConfig {
		
		/* (non-Javadoc)
		 * @see org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter#configure(org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer)
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.inMemory() // 使用in-memory存储
					  .withClient("c3a14543425cbb")
					  .secret("CZbx1vbRkybpdkUObTRN9ktPRDwNOUmv1iG7IyJjOVE=")
					  .authorizedGrantTypes("client_credentials")
					  .scopes("app")
					  .and().withClient("obdada3108b0ee")
					  .secret("57DCE5A6cbc0f48f4376E3F057746577a7c28059f8F=")
					  .authorizedGrantTypes("client_credentials")
					  .scopes("app");
		}
	}

	/**
	 * The Class ResourceServerConfiguration.
	 *
	 * @author Derek
	 * @since 1.0, 2018-9-13
	 */
	@Configuration
	@ConditionalOnWebApplication
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends AbstractResourceServerConfiguration {

		/* (non-Javadoc)
		 * @see com.taoxeo.boot.security.oauth2.AbstractResourceServerConfiguration#configure(org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer)
		 */
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			super.configure(resources);
			resources.resourceId(RESOURCE_ID).stateless(false);
		}

		/* (non-Javadoc)
		 * @see org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
		 */
		@Override
		public void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and()
				.requestMatchers().antMatchers("/api/**","/obd/**","/ws","/stomp")
			.and()
				.authorizeRequests()
				.antMatchers("/api/**","/obd/**").access("hasRole('ROLE_USER') or hasRole('ROLE_WXUSER') or #oauth2.isOAuth()");
			// @formatter:on
		}
	}
}
