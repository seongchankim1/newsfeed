package com.sparta.newsfeed.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed.dto.LoginRequestDto;
import com.sparta.newsfeed.dto.UserRequestDto;
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

// JWT 기반 인증 처리를 위한 필터
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

	// 인증 시도
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
		// 요청 본문에서 사용자명과 비밀번호를 읽음
		Map<String, String> requestBody = objectMapper.readValue(request.getInputStream(), Map.class);
		String username = requestBody.get("username");
		String password = requestBody.get("password");
		User user = userRepository.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException(username));
		if (user.getUser_status().equals("미인증")) {
			throw new IllegalArgumentException("이메일 인증을 먼저 해주세요.");
		}
		// 인증 토큰 생성
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
		// AuthenticationManager 통해 인증 시도
		return getAuthenticationManager().authenticate(authRequest);
	}

	// 인증 성공 처리
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		// 사용자명 가져와서 사용자 조회
		String username = authResult.getName();
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));
		// Access, Refresh 토큰 생성
		String token = jwtUtil.createAccessToken(username);
		user.setRefreshToken(jwtUtil.createRefreshToken(username));
		userRepository.save(user);
		// 응답 헤더에 토큰 추가
		response.addHeader("Authorization", token);
		response.getWriter().write("Login Success! Token : " + token);
	}

	// 인증 실패 처리
	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		// 상태 코드 401
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		// 실패 메시지 출력
		response.getWriter().write("Login failed");
	}
}