package com.wkhmedical.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.alibaba.fastjson.JSONObject;
import com.wkhmedical.util.WebUtil;

import lombok.Setter;

@Setter
public class DefaultAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		if (WebUtil.isAjaxRequest(request)) {
			WebUtil.setJsonResponseConfig(response);
			response.setStatus(HttpServletResponse.SC_OK);
			JSONObject jso = new JSONObject();
			jso.put("status", "ok");
			jso.put("type", "account");
			jso.put("currentAuthority", "admin");
			PrintWriter out = response.getWriter();
			out.append(jso.toJSONString());
			out.flush();
			out.close();
			clearAuthenticationAttributes(request);
		} else {
			super.onAuthenticationSuccess(request, response, authentication);
		}
	}
	
}
