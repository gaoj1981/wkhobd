/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * The Class ConfigProperties.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "biz.cfg")
public class ConfigProperties {

	private Long dbWorkerId;
	private Long dbDatacenterId;

	private String rsaPrivate;
	private String rsaPublic;

	private String uploadPath;

}
