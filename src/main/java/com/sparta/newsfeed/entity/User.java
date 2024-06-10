package com.sparta.newsfeed.entity;
import com.sparta.newsfeed.dto.SignupRequestDto;
import com.sparta.newsfeed.dto.UserRequestDto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
@EntityListeners(AuditingEntityListener.class)
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;  // ID

	@Column(nullable = false, unique = true)
	private String username;  // 사용자 ID

	@Column(nullable = false)
	private String password;  // 비밀번호

	@Column(nullable = false)
	private String name;     // 이름

	@Email
	@NotBlank
	private String email;  // 이메일
	private String nickname; //별칭, 별명

	@Column(nullable = false)
	private String introduce;  // 한 줄 소개

	@Column(nullable = false)
	private String user_status = "정상";     // 회원상태코드

	@Column
	private String refreshToken;

	@Column
	private String accessToken;

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime writeDate;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updateDate;

	@Column
	private LocalDateTime statusChanged;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Newsfeed> newsfeedList = new ArrayList<>();

	public User(String username, String password , String name, String nickname, String email, String introduce, String user_status, String refreshToken, String accessToken) {
	@Column
	private String authKey;

	@Column
	private LocalDateTime verifyTime;

	@Column
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role = UserRoleEnum.USER;

	private LocalDateTime status_changed;    // 상태변경시간

	public void updateStatusChanged() {
		this.status_changed = LocalDateTime.now();
	}


	public User(String username, String password , String nickname, String email, String introduce, String user_status, String refreshToken, String authKey, LocalDateTime verifyTime) {

		this.username = username;
		this.password = password;
		this.name = name;
		this.nickname = nickname;
		this.email = email;
		this.introduce = introduce;
		this.user_status = user_status;
		this.refreshToken = refreshToken;
		this.accessToken = accessToken;
		this.authKey = authKey;
		this.verifyTime = verifyTime;
	}

	public void updateStatus(String user_status) {
		this.user_status = user_status;
		updateStatusChanged();
	}

	public void update(String nickname, String email, String introduce,String password) {
		this.nickname = nickname;
		this.email = email;
		this.introduce = introduce;
		this.password = password;
		updateProfileChanged();
	}

	public void updateStatusChanged() {
		this.statusChanged = LocalDateTime.now();
	}

	public void updateProfileChanged() {
		this.updateDate = LocalDateTime.now();
	}

	public void updateUpdateDate() {
		this.updateDate = LocalDateTime.now();
	}
//
//        public void userProfile(SignupRequestDto requestDto) {
//            this.username = requestDto.getUsername();
//            this.nickname = requestDto.getNickname();
//            this.email = requestDto.getEmail();
//            this.introduce = requestDto.getIntroduce();
//        }
//
//        public void update(String nickname, String email, String introduce,String password) {
//            this.nickname = nickname;
//            this.email = email;
//            this.introduce = introduce;
//            this.password = password;
//            updateProfileChanged();
//        }
}
