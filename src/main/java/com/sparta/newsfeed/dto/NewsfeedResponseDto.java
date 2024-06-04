package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Newsfeed;

public class NewsfeedResponseDto {
    private Long id;
    private String title;
    private String content;

    public NewsfeedResponseDto(Newsfeed savedNewsfeed) {
    }
}
