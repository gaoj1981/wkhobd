package com.wkhmedical.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.alibaba.fastjson.JSONObject;
import com.wkhmedical.exception.BadCaptchaException;
import com.wkhmedical.util.WebUtil;

public class DefaultAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private String defaultFailureUrl;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		if (WebUtil.isAjaxRequest(request)) {
			logger.debug("No failure URL set, sending 401 Unauthorized error");
			WebUtil.setJsonResponseConfig(response);
			response.setStatus(HttpServletResponse.SC_OK);
			JSONObject jso = new JSONObject();
			jso.put("status", "error");
			jso.put("type", "account");
			jso.put("currentAuthority", "guest");
			PrintWriter out = response.getWriter();
			out.append(jso.toJSONString());
			out.flush();
			out.close();
		} else {
			saveException(request, exception);

			if (isUseForward()) {
				logger.debug("Forwarding to " + defaultFailureUrl);

				request.getRequestDispatcher(defaultFailureUrl).forward(request, response);
			} else {
				logger.debug("Redirecting to " + defaultFailureUrl);
				getRedirectStrategy().sendRedirect(request, response,
						exception instanceof BadCaptchaException ? defaultFailureUrl + "_captcha" : defaultFailureUrl);
			}
		}
	}

	public void setDefaultFailureUrl(String defaultFailureUrl) {
		this.defaultFailureUrl = defaultFailureUrl;
	}
}
