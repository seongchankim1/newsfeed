package com.sparta.newsfeed.service;

import com.sparta.newsfeed.entity.Timestamped;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.newsfeed.dto.LoginRequestDto;
import com.sparta.newsfeed.dto.SignupRequestDto;

import java.security.Timestamp;
import java.util.Optional;


@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;


	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

    public void signup(SignupRequestDto requestDto) {

		String username = requestDto.getUsername();
		String password = requestDto.getPassword();
		String email = requestDto.getEmail();
		String introduce = requestDto.getIntroduce();
		String refreshToken = requestDto.getRefreshToken();
		String status = requestDto.getStatus();

		String encodePassword = passwordEncoder.encode(password);

		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		User user = new User(username,encodePassword, email, introduce, status, refreshToken);
		userRepository.save(user);
	}

	public void login(LoginRequestDto requestDto) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);

		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

	}


	public void withdraw(Long id, SignupRequestDto requestDto) {
		User user = userRepository.findById(id).orElseThrow(
				() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);

		if(user.getUser_status().equals("탈퇴")){
			throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
		}

		String password = requestDto.getPassword();
		if(!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		user.updateStatus("탈퇴");
		userRepository.save(user);
	}
}
