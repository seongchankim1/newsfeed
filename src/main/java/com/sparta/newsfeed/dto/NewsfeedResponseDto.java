package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Newsfeed;
import lombok.Getter;

@Getter
public class NewsfeedResponseDto {
    private Long id;
    private String title;
    private String content;

    public NewsfeedResponseDto(Newsfeed newsfeed) {

        this.id = newsfeed.getId();
        this.title = newsfeed.getTitle();
        this.content = newsfeed.getContent();

    }
}
