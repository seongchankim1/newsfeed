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

	// 필터 실행
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 요청에서 JWT 토큰 추출
		String token = jwtUtil.resolveToken(request);

		// 토큰 유효성 검사
		if (token != null && jwtUtil.validateToken(token)) {
			// 인증 객체 생성 및 설정
			Authentication authentication = jwtUtil.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} else {
			System.out.println("토큰이 없습니다. 로그인 해주세요.");
		}

		// 다음 필터 실행
		chain.doFilter(request, response);
	}
}
