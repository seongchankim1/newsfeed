package com.sparta.newsfeed.dto;
import com.sparta.newsfeed.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private String nickname;
    private String email;
    private String introduce;
    private LocalDateTime createAt;

    public UserResponseDto(User user) {

        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
        this.createAt = user.getLastModifiedDateTime();
    }
}
