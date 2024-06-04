package com.sparta.newsfeed.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.newsfeed.dto.LoginRequestDto;
import com.sparta.newsfeed.jwt.JwtUtil;

@RestController
@RequestMapping("/api/auth")
// 기능 구현을 위한 임시 토큰 생성 컨트롤러. 추후 UserController에 합칠 예정
public class AuthController {

	private final JwtUtil jwtUtil;

	public AuthController(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@PostMapping
	public String createAccessToken(@RequestBody LoginRequestDto requestDto) {
		return jwtUtil.createAccessToken(requestDto);
	}

	@PostMapping
	public String createRefreshToken(@RequestBody LoginRequestDto requestDto) {
		return jwtUtil.createRefreshToken(requestDto);
	}
}
