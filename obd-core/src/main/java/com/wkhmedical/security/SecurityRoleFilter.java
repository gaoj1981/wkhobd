package com.wkhmedical.security;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import com.taoxeo.boot.security.SecurityUtils;
import com.wkhmedical.repository.jpa.YunUserRepository;

@Component
public class SecurityRoleFilter extends GenericFilterBean {

	@Resource
	YunUserRepository userRepository;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 继续调用 Filter 链
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String uri = httpRequest.getRequestURI().replace(httpRequest.getContextPath(), "");
		System.out.println(uri + SecurityUtils.getCurrentUser().getRole());
		chain.doFilter(request, response);
	}

}
