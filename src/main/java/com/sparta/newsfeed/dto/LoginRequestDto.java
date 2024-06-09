package com.sparta.newsfeed.dto;

// import com.sparta.newsfeed.entity.UserRoleEnum;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
	private String username;
	private String password;
	private String user_status;
	// private UserRoleEnum role = UserRoleEnum.USER;
}
