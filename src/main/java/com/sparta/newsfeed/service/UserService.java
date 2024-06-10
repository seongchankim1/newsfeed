package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.*;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

 //@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	private final EmailService emailService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
		this.emailService = emailService;
	}

	@Transactional
	public UserResponseDto signup(SignupRequestDto requestDto) throws MessagingException {

		User user = new User();
		user.setUsername(requestDto.getUsername());
		user.setPassword(requestDto.getPassword());
		user.setNickname(requestDto.getNickname());
		user.setEmail(requestDto.getEmail());
		user.setIntroduce(requestDto.getIntroduce());
		user.setRefreshToken(requestDto.getRefreshToken());
		user.setUser_status(requestDto.getUser_status());

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		String authKey = authKeyBuilder();
		user.setAuthKey(authKey);
		emailService.sendVerificationEmail(user.getEmail(), authKey); // 이메일 발송 메서드 호출
		user.setVerifyTime(LocalDateTime.now().plusMinutes(3));

		userRepository.findByUsername(user.getUsername())
				.ifPresent(existingUser -> {
					throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
				});
		userRepository.save(user);
		return new UserResponseDto(user);
	}


	// 로그인
	public String login(LoginRequestDto requestDto, HttpServletResponse response) {
		String username = requestDto.getUsername();
		String password = requestDto.getPassword();

		Optional<User> userOptional = userRepository.findByUsername(username);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (!passwordEncoder.matches(password, user.getPassword())) {
				throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
			}
			String accessToken = jwtUtil.createAccessToken(username);
			response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
			String refreshToken = jwtUtil.createRefreshToken(username);
			user.setRefreshToken(refreshToken);
			userRepository.save(user);
			return "로그인 성공! 토큰 : " + refreshToken;
		} else {
			throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
		}
	}

	// 회원 탈퇴
	public void withdraw(UserRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		String newAccessToken = jwtUtil.refreshToken(token, response);
		String newBearerAccessToken = jwtUtil.substringToken(newAccessToken);
		String username = jwtUtil.getUserInfoFromToken(newBearerAccessToken).getSubject();
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("등록된 사용자가 없습니다.")
		);

		if (user.getUser_status().equals("탈퇴")) {
			throw new IllegalArgumentException("이미 탈퇴한 사용자입니다.");
		}
		String password = requestDto.getPassword();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		user.updateStatus("탈퇴");
		userRepository.save(user);
	}

	//유저 프로필 조회
	public UserResponseDto findUser(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(()
			-> new IllegalArgumentException("해당 User가 존재하지않습니다."));
		return new UserResponseDto(user);
	}

	;

	//  프로필 update 코드
	@Transactional
	public UserUpdateResponseDto profileUpdate(UserUpdateRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
		String token = jwtUtil.resolveToken(request);
		String newAccessToken = jwtUtil.refreshToken(token, response);
		String newBearerAccessToken = jwtUtil.substringToken(newAccessToken);
		String username = jwtUtil.getUserInfoFromToken(newBearerAccessToken).getSubject();
		User user = userRepository.findByUsername(username).orElseThrow(()
			-> new IllegalArgumentException("존재하지 않는 사용자입니다."));

		String password = requestDto.getPassword();
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}

		password = requestDto.getPassword();
		String newPassword = passwordEncoder.encode(password);
		user.setPassword(newPassword);
		userRepository.save(user);
		user.updateUpdateDate();
		user.update(
			requestDto.getNickname(),
			requestDto.getEmail(),
			requestDto.getIntroduce(),
			user.getPassword()
		);
		if (passwordEncoder.matches(newPassword, user.getPassword())) {
			throw new IllegalArgumentException("동일한 비밀번호는 사용하실 수 없습니다");
		}
		return new UserUpdateResponseDto(user);
	}


	//authKey 생성
	public String authKeyBuilder() {
		StringBuilder stringBuilder = new StringBuilder();
		// 6자리 숫자 코드
		stringBuilder.append((int)(Math.random()*10));
		stringBuilder.append((int)(Math.random()*10));
		stringBuilder.append((int)(Math.random()*10));
		stringBuilder.append((int)(Math.random()*10));
		stringBuilder.append((int)(Math.random()*10));
		stringBuilder.append((int)(Math.random()*10));

		String authKey = stringBuilder.toString();
		System.out.println(authKey);
		return authKey;
	}

	//이메일 인증 시간 제한
	@Transactional
	public String verifyMail(VerifyRequestDto requestDto) {
		User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() -> new IllegalArgumentException("일치하는 아이디가 없습니다."));
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
		}
		if (!requestDto.getAuthKey().equals(user.getAuthKey())) {
			throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
		}
		user.setUser_status("정상");
		if (LocalDateTime.now().isAfter(user.getVerifyTime())) {
			throw new IllegalArgumentException("인증 시간이 초과되었습니다.");
		}
		userRepository.save(user);


		return "인증 완료!";
	}
}


