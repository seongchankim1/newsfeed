package com.sparta.newsfeed.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Map;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, ObjectMapper objectMapper, UserRepository userRepository) {
		super(new AntPathRequestMatcher("/api/user/login"));
		setAuthenticationManager(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
		String username = requestBody.get("username");
		String password = requestBody.get("password");
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		return getAuthenticationManager().authenticate(authRequest);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		String username = authResult.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
		String token = jwtUtil.createAccessToken(username);
		user.setRefreshToken(jwtUtil.createRefreshToken(username));
		userRepository.save(user);
		response.addHeader("Authorization", token);
		response.getWriter().write("Login Success! Token : " + token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write("Login failed");
	}
}
