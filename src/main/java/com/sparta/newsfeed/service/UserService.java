package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service

public class UserService {
	private final UserRepository userRepository;

	public void signup(SignupRequestDto requestDto) {

		String username = requestDto.getUsername();
		String password = requestDto.getPassword();
		String nickname = requestDto.getNickname();
		String email = requestDto.getEmail();
		String introduce = requestDto.getIntroduce();
		String status = "정상";

		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		User user = new User(username, password, nickname, email, introduce );
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
	//유저 프로필 조회
	public UserResponseDto findUser (String username) {
		User user = userRepository.findByUsername(username).orElseThrow(()
				-> new IllegalArgumentException("해당 User가 존재하지않습니다."));
		return new UserResponseDto(user);
	};

	//  프로필 update 코드
	@Transactional
	public UserUpdateResponseDto profileUpdate(String username, UserUpdateRequestDto requestDto) {
		User user = userRepository.findByUsername(username).orElseThrow(()
				-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
		if (user.getPassword().equals(user.getPassword())) {
			user.setPassword(requestDto.getChangePassword());}
		else{
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		user.update(
				requestDto.getNickname(),
				requestDto.getEmail(),
				requestDto.getIntroduce(),
				requestDto.getPassword()
		);
		if (user.getPassword().equals(requestDto.getChangePassword())){
			throw new IllegalArgumentException("동일한 비밀번호는 사용하실 수 없습니다");
		}
		return new UserUpdateResponseDto(user);
	}
}


