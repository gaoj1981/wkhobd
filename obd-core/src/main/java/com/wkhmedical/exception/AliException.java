package com.wkhmedical.exception;

import com.taoxeo.lang.exception.BizRuntimeException;

public class AliException extends BizRuntimeException {
	private static final long serialVersionUID = -7281069626716776938L;

	public AliException(String code, Object... values) {
		super(code, values);
	}
}
