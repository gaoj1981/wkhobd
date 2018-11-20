/**
 * 
 */
package com.wkhmedical;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.taoxeo.boot.security.SecurityUtils;
import com.wkhmedical.repository.jpa.YunUserRepository;

import lombok.extern.log4j.Log4j2;

/**
 * @author Administrator
 */
@Log4j2
@Component
public class SecurityIdentityHandlerInterceptor extends HandlerInterceptorAdapter {

	@Resource
	SessionRepository<Session> sessionRepository;

	@Resource
	YunUserRepository userRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (request.getRequestURI().startsWith("/public/")) return true;
		log.info(SecurityUtils.getCurrentUser().getRole() + " " + request.getRequestURI());
		return true;
	}

}
