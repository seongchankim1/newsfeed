package com.sparta.newsfeed.dto;

import java.sql.Timestamp;

import lombok.Getter;

@Getter

public class UserRequestDto {
    private String nickname;
    private String name;
    private String password;
    private String email;
    private String introduce;
    private Timestamp updated;
}
