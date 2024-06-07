package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
