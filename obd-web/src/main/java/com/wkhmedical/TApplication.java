/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.taoxeo.boot.BootAutoConfig;

/**
 * The Class TApplication.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@SpringBootApplication
@BootAutoConfig
public class TApplication implements WebMvcConfigurer {

	@Autowired
	SecurityIdentityHandlerInterceptor securityIdentityHandlerInterceptor;
	@Value("${spring.session.single-identity:true}")
	private Boolean sessionSingleIdentity;

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TApplication.class, args);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (sessionSingleIdentity) {
			registry.addInterceptor(securityIdentityHandlerInterceptor);
		}
	}

}
