package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Like;

public class LikeResponseDto {
    private String uername;
    private int likes;
    private long id;

    public LikeResponseDto(Like like) {
        this.uername = like.getUser().getUsername();
        this.likes = like.getNewsfeed().getLikes();
        this.id = like.getNewsfeed().getId();
    }
}
