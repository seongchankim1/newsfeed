package com.sparta.newsfeed.dto;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    private String comment;

    public CommentUpdateRequest(Long id, String comment) {
        this.comment = comment;
    }
}
