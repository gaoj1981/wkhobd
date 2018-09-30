package com.wkhmedical.exception;

import com.taoxeo.lang.exception.BizRuntimeException;

public class PublicException extends BizRuntimeException {

	private static final long serialVersionUID = -6144899881867305414L;

	public PublicException(String code, Object... values) {
		super(code, values);
	}
}
