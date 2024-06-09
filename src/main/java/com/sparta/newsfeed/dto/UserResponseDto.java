package com.sparta.newsfeed.dto;
import com.sparta.newsfeed.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto {
    private String username;
    private String name;
    private String email;
    private String introduce;

    public UserResponseDto(User user) {

        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
    }
}
