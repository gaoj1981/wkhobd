package com.wkhmedical.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("securityResourceDecisionHandler")
public class SecurityResourceDecisionHandler {
	/**
	 * 保存的是url以及url所需要的权限
	 */
	private static final Map<String, List<GrantedAuthority>> URL_AUTHS = new ConcurrentHashMap<>();

	static {
		URL_AUTHS.put("/api/car/**", Arrays.asList(new SimpleGrantedAuthority("ADMIN")));
		URL_AUTHS.put("/api/binduser/**", Arrays.asList(new SimpleGrantedAuthority("USER")));
	}
	AntPathMatcher pathMatcher = new AntPathMatcher();

	/**
	 * 自定义决策
	 * 
	 * @param authentication 认证对象
	 * @param request 请求的request对象
	 * @return true:有权限访问 false:无权限访问
	 */
	public boolean auth(Authentication authentication, HttpServletRequest request) {
		String uri = request.getRequestURI().replace(request.getContextPath(), "");
		for (Entry<String, List<GrantedAuthority>> entry : URL_AUTHS.entrySet()) {
			if (pathMatcher.match(entry.getKey(), uri)) {
				Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				for (GrantedAuthority grantedAuthority : authorities) {
					if (entry.getValue().contains(grantedAuthority)) {
						return true;
					}
				}
				log.error("当前访问的uri:{},需要的权限是:{},当前用户无此权限.", uri, entry.getValue());
				return false;
			}
		}

		// 访问的是没有配置权限的功能，必须要登录用户才可以进行访问
		if (authentication.isAuthenticated() && !Objects.equals("anonymousUser", authentication.getPrincipal())) {
			return true;
		}

		// 没有登录，直接返回false
		return false;
	}
}
