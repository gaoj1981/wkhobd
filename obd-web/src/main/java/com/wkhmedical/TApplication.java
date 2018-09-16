/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.taoxeo.boot.BootAutoConfig;

import lombok.extern.log4j.Log4j2;

/**
 * The Class TApplication.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@Log4j2
@SpringBootApplication
@BootAutoConfig
public class TApplication implements ApplicationRunner {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TApplication.class, args);
	}

	/* (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("Application Startup");
	}
}
