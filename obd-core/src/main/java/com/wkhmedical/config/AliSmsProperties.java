package com.wkhmedical.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.sms")
public class AliSmsProperties {

	private String accessKeyId;

	private String accessKeySecret;

	private String product;

	private String domain;

	private String endpointName;

	private String endpointRegion;

}
