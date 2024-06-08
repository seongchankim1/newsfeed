package com.sparta.newsfeed.security;

import com.sparta.newsfeed.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		String token = jwtUtil.resolveToken(request);

		if (token != null && jwtUtil.validateToken(token)) {
			Authentication authentication = jwtUtil.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			System.out.println("토큰이 없습니다. 로그인 해주세요.");
		}

		chain.doFilter(request, response);
	}
}
