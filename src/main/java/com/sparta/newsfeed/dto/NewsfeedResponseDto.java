package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Newsfeed;
import lombok.Getter;

@Getter
public class NewsfeedResponseDto {
    private Long id;
    private String title;
    private String content;

    public NewsfeedResponseDto(Long id, String title, String contents) {
        this.id = id;
        this.title = title;
        this.content = contents;
    }

    public NewsfeedResponseDto(Newsfeed newsfeed) {

        this.id = newsfeed.getId();
        this.title = newsfeed.getTitle();
        this.content = newsfeed.getContent();
    }

        public static NewsfeedResponseDto toDto(Newsfeed newsfeed) {
        return new NewsfeedResponseDto(
                newsfeed.getId(),
                newsfeed.getTitle(),
                newsfeed.getContent());

    }
}