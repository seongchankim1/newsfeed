package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.User;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.sql.Timestamp;


@Getter
@Setter
public class UserUpdateRequestDto {

    private String nickname;
    private String email;
    private String introduce;
    private Timestamp updated;

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{10,}$",
            message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함하고 최소 10글자 이상이여야 합니다.")
    private String password;

    // private String changePassword;
    private LocalDateTime createAt;


//    public UserUpdateRequestDto(User user) {
//        this.nickname = nickname;
//        this.email = email;
//        this.introduce = introduce;
//        this.password = password;
//    }

    public UserUpdateRequestDto(String nickname, String email, String introduce, String password) {
        this.nickname = nickname;
        this.email = email;
        this.introduce = introduce;
        this.password = password;
    }
}
