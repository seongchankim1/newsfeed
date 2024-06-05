package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.service.UserService;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final UserRepository userRepository;

	public UserController(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	// 회원가입
	@PostMapping("/signup")
	public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto requestDto) {
		userService.signup(requestDto);
		return ResponseEntity.ok("회원가입 완료!");
	}

	// 회원탈퇴
	@PutMapping("/withdraw/{id}")
	public ResponseEntity<String> withdraw(@PathVariable Long id, @RequestBody @Valid SignupRequestDto requestDto) {
		userService.withdraw(id, requestDto);
		return ResponseEntity.ok("회원탈퇴 완료!");
	}

	// 로그인
	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
		// String token = userService.login(requestDto);
		userService.login(requestDto);
		return ResponseEntity.ok("로그인 완료!");
	}

	@GetMapping("/{username}/profile") //조회기능 구현위치
	public ResponseEntity<UserResponseDto> getProfiles(
			@PathVariable String username) {
		return ResponseEntity.ok().body(userService.findUser(username));
	}

	@PutMapping("/{username}/profile")//수정기능 구현위치
	public ResponseEntity<UserUpdateResponseDto> updateProfiles(
			@PathVariable String username,
			@RequestBody UserUpdateRequestDto requestDto) {
		return ResponseEntity.ok().body(userService.profileUpdate(username, requestDto));
	}
}
