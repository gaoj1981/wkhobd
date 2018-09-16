package com.wkhmedical.exception;

import com.taoxeo.lang.exception.BizRuntimeException;

public class ObdLicException extends BizRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2844052232694281522L;

	public ObdLicException(String code, Object... values) {
		super(code, values);
	}
}
