package com.sparta.newsfeed.dto;

import com.sparta.newsfeed.entity.Comment;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter

public class CommentResponse {
    private long id;
    private String title;
    private String comment;
    private long good_counting;

    public CommentResponse(long id, String title, String comment, long good_counting) {
        this.id = id;
        this.title = title;
        this.comment = comment;
        this.good_counting = good_counting;
    }

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.title = comment.getNewsfeed().getTitle();
        this.comment = comment.getComment();
    }

    public static CommentResponse toDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getNewsfeed().getTitle(),
                comment.getComment(),
                comment.getGood_counting()
        );
    }
}
