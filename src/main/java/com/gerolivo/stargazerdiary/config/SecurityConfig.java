package com.gerolivo.stargazerdiary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Value( "${restapi.path}" )
	private String apiPath;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/favicon.ico", "/register", "/h2-console/**", "/webjars/**", "/css/**", "/js/**").permitAll()
                .antMatchers(apiPath + "/registration/register").permitAll()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
            .logout()
                .permitAll();
        http.cors();
        // Enable authentication for REST API
		http
			.authorizeRequests()
				.antMatchers(apiPath + "/**").authenticated()
				.and()
			.httpBasic();
        // Prevent redirection to login page for non-authenticated REST API requests
		http.exceptionHandling()
        	.defaultAuthenticationEntryPointFor(getRestApiAuthenticationEntryPoint(), new AntPathRequestMatcher(apiPath + "/**"));
        
        // For Rest API and H2 Console 
        http.csrf().ignoringAntMatchers(apiPath + "/**", "/h2-console/*");
        http.headers().frameOptions().sameOrigin();
    }
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder(11);
	}
	
	private AuthenticationEntryPoint getRestApiAuthenticationEntryPoint() {
        return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
    }
	
}
