/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.web.api;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * The Class PublicController.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@RestController
@Api(tags = "公共接口")
@RequestMapping("/public")
public class PublicController {

	/**
	 * dateNow.
	 *
	 * @return the date
	 */
	@ApiOperation(value = "服务端系统时间")
	@GetMapping("/sys/date")
	public Date dateNow() {
		return new Date();
	}
}
