package com.sparta.newsfeed.dto;

import lombok.Getter;

@Getter

public class ProfileRequestDto {
    private Long id;
    private String name;
    private String nickname;
    private String password;
    private String email;
    private String mention;
}
