package com.sparta.newsfeed.dto;

import java.sql.Timestamp;
import com.sparta.newsfeed.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserUpdateResponseDto {
    private String nickname;
    private String email;
    private String introduce;
    // private LocalDateTime modfiledData;


    public UserUpdateResponseDto(User user) {

        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.introduce = user.getIntroduce();
        //this.modfiledData = user.getLastModifiedDateTime();
    }
}
