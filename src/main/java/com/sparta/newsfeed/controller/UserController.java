package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.service.UserService;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;


import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto) throws MessagingException {
		userService.signup(requestDto);
		return ResponseEntity.ok("회원가입 완료! 이메일 인증을 해주세요.");
	}

	// 회원탈퇴
	@DeleteMapping("/withdraw")
	public ResponseEntity<String> withdraw(@RequestBody UserRequestDto requestDto,HttpServletResponse response, HttpServletRequest request) {
		userService.withdraw(requestDto, response, request);
		return ResponseEntity.ok("회원탈퇴 완료!");
	}

	// 로그인
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
		// String token = userService.login(requestDto);
		return ResponseEntity.ok(userService.login(requestDto, response));
	}

	@GetMapping("/{username}/profile") //조회기능 구현위치
	public ResponseEntity<UserResponseDto> getProfiles(
			@PathVariable String username) {
		return ResponseEntity.ok().body(userService.findUser(username));
	}

	@PutMapping("/profile")//수정기능 구현위치
	public ResponseEntity<UserUpdateResponseDto> updateProfiles(
			@RequestBody UserUpdateRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
		return ResponseEntity.ok().body(userService.profileUpdate(requestDto, response, request));
	}

	@PostMapping("/verify")
	public String verifyMail(@RequestBody VerifyRequestDto requestDto) {
		return userService.verifyMail(requestDto);
	}
}
