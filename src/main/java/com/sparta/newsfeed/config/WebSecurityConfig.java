package com.sparta.newsfeed.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.repository.UserRepository;
import com.sparta.newsfeed.security.JwtAuthenticationFilter;
import com.sparta.newsfeed.security.JwtAuthorizationFilter;
import com.sparta.newsfeed.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	private final UserDetailsServiceImpl userDetailsService;
	private final JwtUtil jwtUtil;
	private final ObjectMapper objectMapper;
	private final UserRepository userRepository;

	// WebSecurityConfig 초기화
	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, ObjectMapper objectMapper, UserRepository userRepository) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
		this.objectMapper = objectMapper;
		this.userRepository = userRepository;
	}

	// SecurityFilterChain 빈 정의
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		// AuthenticationManager 설정
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

		http.csrf(csrf -> csrf.disable()) // CSRF 비활성화
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 안씀
			.authorizeHttpRequests(authorize -> authorize
				// 특정 경로에 대한 접근 권한 설정
				.requestMatchers("/api/user/signup", "/api/user/login", "/api/user/verify", "/swagger-ui/**").permitAll()
				.anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
			)
			// JWT 인증 필터 추가
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil, objectMapper, userRepository), UsernamePasswordAuthenticationFilter.class)
			// JWT 인가 필터 추가
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// AuthenticationManager 빈 정의
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
