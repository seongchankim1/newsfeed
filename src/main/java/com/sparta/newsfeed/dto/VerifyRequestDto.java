package com.sparta.newsfeed.dto;

import lombok.Getter;

@Getter
public class VerifyRequestDto {
	private String username;
	private String password;
	private String authKey;

	public VerifyRequestDto(String username, String password, String authKey) {
		this.username = username;
		this.password = password;
		this.authKey = authKey;
	}
}
