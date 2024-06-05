package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

	@Column(nullable = false)
	private String introduce;  // 한 줄 소개

	@Column(nullable = false)
	private String user_status;     // 회원상태코드

	@Column(nullable = false)
	private String refreshToken;



	public User(String username, String password , String email, String introduce, String user_status, String refreshToken) {

		this.username = username;
		this.password = password;
		this.email = email;
		this.introduce = introduce;
		this.user_status = user_status;
		this.refreshToken = refreshToken;
	}

	public void updateStatus(String user_status) {
		this.user_status = user_status;
		updateStatusChanged();
	}
}
