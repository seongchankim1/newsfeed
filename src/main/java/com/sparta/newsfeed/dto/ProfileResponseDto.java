package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Profile;

import java.time.LocalDateTime;

public class ProfileResponseDto {
//    private Long id;
//    private String name;
    private String nickname;
//    private String password;
    private String email;
    private String mention;
    private LocalDateTime createAt;

    public ProfileResponseDto(Profile profile) {
//        this.id = profile.getId();
//        this.name = profile.getName();
        this.nickname = profile.getNickname();
//        this.password = profile.getPassword();
        this.email = profile.getEmail();
        this.mention = profile.getMention();
        this.createAt = profile.getCreatedAt();
    }
}
