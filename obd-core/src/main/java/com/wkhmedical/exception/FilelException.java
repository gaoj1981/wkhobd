/**
 * 
 */
package com.wkhmedical.exception;

import com.taoxeo.lang.exception.BizRuntimeException;

/**
 * @author Administrator
 */
public class FilelException extends BizRuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FilelException(String code, Object... values) {
		super(code, values);
	}
}
