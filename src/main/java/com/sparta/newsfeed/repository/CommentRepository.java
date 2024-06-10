package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Comment;
import com.sparta.newsfeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Newsfeed findByUsername(String username);

}
