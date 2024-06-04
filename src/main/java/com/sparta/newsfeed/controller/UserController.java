package com.sparta.newsfeed.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.newsfeed.dto.LoginRequestDto;
import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.service.UserService;

@Controller
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto) {
		userService.signup(requestDto);
		return ResponseEntity.ok("회원가입 완료!");
	}

	// 로그인
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
		// String token = userService.login(requestDto);
		userService.login(requestDto);
		return ResponseEntity.ok("로그인 완료!");
	}
}
