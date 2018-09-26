package com.wkhmedical.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {

	public static final String AppJsonRequest = "application/json";

	/**
	 * 判断请求是否是AJAX请求.
	 *
	 * @param request
	 *            the request
	 * @return true, if is ajax request
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String xreq = request.getHeader("content-type");
		if (xreq != null && xreq.indexOf(AppJsonRequest) >= 0)
			return true;
		return false;
	}

	public static void setHtmlResponseConfig(HttpServletResponse response) {
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
	}

	public static void setJsonResponseConfig(HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
