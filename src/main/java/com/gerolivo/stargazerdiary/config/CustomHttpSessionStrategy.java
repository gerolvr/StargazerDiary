package com.gerolivo.stargazerdiary.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.session.Session;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * Required to handle both cookie and token-based authentication
 * Depending on the url path, provide the relevant *HttpSessionStrategy
 * i.e.
 * - www.exemple.com/hello --> CookieHttpSessionStrategy
 * - www.exemple.com/api/v1/hello --> HeaderHttpSessionStrategy
 * 
 * @author gerolvr
 *
 */
//@Component
public class CustomHttpSessionStrategy implements HttpSessionStrategy {

	@Value("${restapi.path}")
	private String restApiPath;
	
	private HttpSessionStrategy cookieHttpSessionStrategy;

	private HttpSessionStrategy headerHttpSessionStrategy;
	
	
	public CustomHttpSessionStrategy(CookieHttpSessionStrategy cookieHttpSessionStrategy, HeaderHttpSessionStrategy headerHttpSessionStrategy) {
		this.cookieHttpSessionStrategy = cookieHttpSessionStrategy;
		this.headerHttpSessionStrategy = headerHttpSessionStrategy;
	}

	@Override
	public String getRequestedSessionId(HttpServletRequest request) {
		return getSessionStrategy(request).getRequestedSessionId(request);
	}

	@Override
	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		getSessionStrategy(request).onNewSession(session, request, response);
	}

	@Override
	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		getSessionStrategy(request).onInvalidateSession(request, response);
	}
	
	private HttpSessionStrategy getSessionStrategy(HttpServletRequest request) {
		return request.getRequestURI().startsWith(restApiPath) ? headerHttpSessionStrategy : cookieHttpSessionStrategy;
	}

}
