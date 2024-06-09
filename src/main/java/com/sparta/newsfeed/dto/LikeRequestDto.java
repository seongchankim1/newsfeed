package com.sparta.newsfeed.dto;

import lombok.Getter;

@Getter
public class LikeRequestDto {
    private Long Username;
    private Long id;
    private String title;
    private String content;
    private int likes;
}
