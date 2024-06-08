package com.sparta.newsfeed.config;

import com.sparta.newsfeed.jwt.JwtUtil;
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

	public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil) {
		this.userDetailsService = userDetailsService;
		this.jwtUtil = jwtUtil;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
		AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();

		http.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/api/auth/signup", "/api/auth/login").permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(new JwtAuthenticationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(new JwtAuthorizationFilter(authenticationManager, jwtUtil), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
