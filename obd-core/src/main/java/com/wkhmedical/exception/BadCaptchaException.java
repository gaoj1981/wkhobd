package com.wkhmedical.exception;

import org.springframework.security.core.AuthenticationException;

public class BadCaptchaException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4318843246226989984L;

	public BadCaptchaException() {
		super("验证码错误");
	}
}
