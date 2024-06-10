package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.entity.User;
import lombok.Getter;

@Getter

public class CommentCreateRequest {
    private String comment;
    private String username;

    public CommentCreateRequest( String comment, String username) {
        this.comment = comment;
        this.username = username;
    }
}
