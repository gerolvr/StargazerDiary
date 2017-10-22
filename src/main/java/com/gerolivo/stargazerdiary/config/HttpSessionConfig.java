package com.gerolivo.stargazerdiary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.ExpiringSession;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

@Configuration
@EnableSpringHttpSession
public class HttpSessionConfig {
	
	@Bean
	public SessionRepository<ExpiringSession> inmemorySessionRepository() {
		return new MapSessionRepository();
	}

	/**
	 * Required to handle both cookie and token-based authentication
	 */
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		return new CustomHttpSessionStrategy(new CookieHttpSessionStrategy(), new HeaderHttpSessionStrategy());
	}
}