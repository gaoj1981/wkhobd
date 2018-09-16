/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.web.client.RestTemplate;

import com.taoxeo.repository.JpaConfig;
import com.taoxeo.repository.JpaRepositoryFactoryBean;
import com.taoxeo.repository.MongoConfig;
import com.taoxeo.repository.MongoRepositoryFactoryBean;

/**
 * The Class TConfiguration.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Configuration
@ImportResource(locations = { "classpath:sql_config.xml" })
@EnableConfigurationProperties({ JpaProperties.class })
@EnableRedisRepositories(basePackages = { "com.wkhmedical.repository.redis" })
@EnableMongoRepositories(basePackages = { "com.wkhmedical.repository.mongo" }, repositoryFactoryBeanClass = MongoRepositoryFactoryBean.class)
@EnableJpaRepositories(basePackages = { "com.wkhmedical.repository.jpa" }, repositoryFactoryBeanClass = JpaRepositoryFactoryBean.class)
@Import({ JpaConfig.class, MongoConfig.class })
public class TConfiguration {
	
	/** The application context. */
	@Resource
	ApplicationContext applicationContext;

	/**
	 * Rest template.
	 *
	 * @return the rest template
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * Cache manager customizer.
	 *
	 * @return the cache manager customizer
	 */
	@Bean
	public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
		return new CacheManagerCustomizer<RedisCacheManager>() {
			@Override
			public void customize(RedisCacheManager cacheManager) {
			}
		};
	}

	/**
	 * Jpa properties.
	 *
	 * @param jpaProperties the jpa properties
	 * @return the properties
	 */
	@Bean("jpaProperties")
	public Properties jpaProperties(JpaProperties jpaProperties) {
		Properties properties = new Properties();
		properties.putAll(jpaProperties.getProperties());
		return properties;
	}

}
