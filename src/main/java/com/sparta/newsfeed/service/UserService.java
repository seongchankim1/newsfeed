package com.sparta.newsfeed.service;

import com.sparta.newsfeed.entity.Timestamped;
import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Timestamp;
import java.util.Optional;

@RequiredArgsConstructor
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
		String nickname = requestDto.getNickname();
		String email = requestDto.getEmail();
		String introduce = requestDto.getIntroduce();
		String refreshToken = requestDto.getRefreshToken();
		String status = requestDto.getStatus();

		String encodePassword = passwordEncoder.encode(password);

		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		User user = new User(username, password, nickname, email, introduce );
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


