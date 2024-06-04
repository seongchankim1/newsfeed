package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.ProfileRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor
public class Profile extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String nickname;
    private String password;
    private String email;
    private String mention;

    public void profile(ProfileRequestDto requestDto) {
        this.name = requestDto.getName();
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
        this.email = requestDto.getEmail();
        this.mention = requestDto.getMention();
    }

    public void update(ProfileRequestDto requestDto) {
        this.nickname = requestDto.getNickname();
        this.password = requestDto.getPassword();
        this.email = requestDto.getEmail();
        this.mention = requestDto.getMention();
    }
}
