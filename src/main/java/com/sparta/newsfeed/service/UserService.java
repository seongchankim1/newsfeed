package com.sparta.newsfeed.service;

import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.sparta.newsfeed.dto.LoginRequestDto;
import com.sparta.newsfeed.dto.SignupRequestDto;

import java.util.Optional;


@Service
public class UserService {
	private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void signup(SignupRequestDto requestDto) {

		String username = requestDto.getUsername();
		String password = requestDto.getPassword();
		String email = requestDto.getEmail();
		String introduce = requestDto.getIntroduce();
		String status = requestDto.getStatus();

		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		User user = new User(username,password, email, introduce, status);
		userRepository.save(user);
	}

	public void login(LoginRequestDto requestDto) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

		User user = userRepository.findByUsername(username).orElseThrow(
				() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);

		if (!user.getPassword().equals(password)) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

	}



}
