/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.web.api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class PublicController.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@RestController
@RequestMapping("/public/**")
public class PublicController {
	
	/**
	 * Info.
	 *
	 * @return the date
	 */
	@GetMapping("info")
	public Date info() {
		return new Date();
	}
}
