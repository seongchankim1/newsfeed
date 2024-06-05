package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserUpdateRequestDto {

    private String nickname;
    private String email;
    private String introduce;
    private String password;
    private String changePassword;
    private LocalDateTime createAt;


    public UserUpdateRequestDto(User user) {
        this.nickname = nickname;
        this.email = email;
        this.introduce = introduce;
        this.password = password;
    }

    public UserUpdateRequestDto(String nickname, String email, String introduce, String password) {
        this.nickname = nickname;
        this.email = email;
        this.introduce = introduce;
        this.password = password;
    }
}
