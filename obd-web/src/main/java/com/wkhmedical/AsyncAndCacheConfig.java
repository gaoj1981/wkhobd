/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * The Class AsyncAndCacheConfig.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Configuration
@ConditionalOnWebApplication
@EnableAsync
@EnableCaching
@EnableScheduling
public class AsyncAndCacheConfig implements AsyncConfigurer, SchedulingConfigurer {
	
	/** The async pool core size. */
	@Value("${async.pool.core_size:2}")
	private Integer async_pool_core_size;

	/** The async pool max size. */
	@Value("${async.pool.max_size:50}")
	private Integer async_pool_max_size;

	/** The async pool delay. */
	@Value("${async.pool.delay:500}")
	private Integer async_pool_delay;

	/** The scheduled pool size. */
	@Value("${scheduled.pool.size:2}")
	private Integer scheduled_pool_size;

	/**
	 * Async executor bean.
	 *
	 * @return the executor
	 */
	@Bean(destroyMethod = "shutdown", name = "asyncExecutor")
	public Executor asyncExecutorBean() {
		// 异步延迟队列
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(async_pool_core_size) {
			@Override
			public <T> Future<T> submit(Callable<T> task) {
				if (async_pool_delay > 0) return super.schedule(task, async_pool_delay, TimeUnit.MILLISECONDS);
				else return super.submit(task);
			}
		};
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("AsyncExecutor-");
		executor.setThreadFactory(threadFactory);
		executor.setMaximumPoolSize(async_pool_max_size);
		return executor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncExecutor()
	 */
	@Override
	public Executor getAsyncExecutor() {
		return asyncExecutorBean();
	}

	/**
	 * Task executor.
	 *
	 * @return the executor
	 */
	@Bean(destroyMethod = "shutdown")
	public Executor taskExecutor() {
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(scheduled_pool_size);
		CustomizableThreadFactory threadFactory = new CustomizableThreadFactory("TaskExecutor-");
		executor.setThreadFactory(threadFactory);
		return executor;
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.annotation.SchedulingConfigurer#configureTasks(org.springframework.scheduling.config.ScheduledTaskRegistrar)
	 */
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskExecutor());
	}

	/* (non-Javadoc)
	 * @see org.springframework.scheduling.annotation.AsyncConfigurer#getAsyncUncaughtExceptionHandler()
	 */
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				ex.printStackTrace();
			}
		};
	}
}
