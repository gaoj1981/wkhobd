/*
 * Copyright (C) 2018 TaoXeo. All rights reserved.
 */
package com.wkhmedical.web;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.taoxeo.boot.DefaultErrorHandler;

/**
 * The Class ErrorHandler.
 *
 * @author Derek
 * @since 1.0, 2018-9-13
 */
@RestControllerAdvice({ "com.wkhmedical.web.api"})
public class ErrorHandler extends DefaultErrorHandler {

}
