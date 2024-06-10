package com.sparta.newsfeed.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsfeedRequestDto {
        private String username;
        private String title;
        private String content;

        public NewsfeedRequestDto(String username, String title, String content) {
                this.username = username;
                this.title = title;
                this.content = content;
        }
}