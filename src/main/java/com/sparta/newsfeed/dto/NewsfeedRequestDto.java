package com.sparta.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsfeedRequestDto {
        private String title;
        private String content;
        private int like;

        public NewsfeedRequestDto(String title, String content, int like) {
                this.title = title;
                this.content = content;
                this.like = like;
        }
}