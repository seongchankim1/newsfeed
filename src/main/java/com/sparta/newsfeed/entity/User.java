package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.dto.UserRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class User extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // ID

	@Column(nullable = false, unique = true)
	private String username;  // 사용자 ID

	@Column(nullable = false)
	private String password;  // 비밀번호

	@Email
	@NotBlank
	private String email;  // 이메일
	private String nickname; //별칭, 별명

	@Column(nullable = false)
	private String introduce;  // 한 줄 소개

	@Column(nullable = false)
	private String user_status;     // 회원상태코드

	@Column(nullable = false)
	private String refreshToken;



	public User(String username, String password , String nickname, String email, String introduce, String user_status, String refreshToken) {

		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.email = email;
		this.introduce = introduce;
		this.user_status = user_status;
		this.refreshToken = refreshToken;
	}

	public void updateStatus(String user_status) {
		this.user_status = user_status;
		updateStatusChanged();
	}

	public void userProfile(SignupRequestDto requestDto) {
		this.username = requestDto.getUsername();
		this.nickname = requestDto.getNickname();
		this.email = requestDto.getEmail();
		this.introduce = requestDto.getIntroduce();
	}

	public void update(String nickname, String email, String introduce,String password) {
		this.nickname = nickname;
		this.email = email;
		this.introduce = introduce;
		this.password = password;
		updateProfileChanged();
		// LocalDateTime lastModifiedDateTime = this.getLastModifiedDateTime();
	}
}
