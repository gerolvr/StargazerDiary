package com.gerolivo.stargazerdiary.restcontrollers;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gerolivo.stargazerdiary.repositories.StargazerRepository;

@RestController
@RequestMapping("${restapi.path}")
public class LoginRestController {
	
	StargazerRepository stargazerRepository;

	public LoginRestController(StargazerRepository stargazerRepository) {
		this.stargazerRepository = stargazerRepository;
	}

	
	@GetMapping("/user/getToken")
	public Map<String, String> getToken(HttpSession session, HttpServletRequest request/*, @AuthenticationPrincipal User user*/) {
		return Collections.singletonMap("token", session.getId());
	}
	
	/**
	 * Only request with a valid token and session can reach that endpoint and
	 * get the HttpStatus.OK response 
	 */
	@GetMapping("/user/checkSession")
	public ResponseEntity<String> checkSession() {
		return new ResponseEntity<String>("Session Active!", HttpStatus.OK);
	}
	
	@GetMapping("/user/getCurrentUserName")
	public Map<String, String> getCurrentUserName(@AuthenticationPrincipal User user) {
		String userName = stargazerRepository.findByUserName(user.getUsername()).getUserName();
		return Collections.singletonMap("userName", userName);
	}
	
	@PostMapping("/user/logout")
	public ResponseEntity<String> logout(){
		SecurityContextHolder.clearContext();
		return new ResponseEntity<String>("Logout Successfully!", HttpStatus.OK);
	}
}
