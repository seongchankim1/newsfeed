package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Comment;
import com.sparta.newsfeed.entity.Like;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByUserAndNewsfeed(User user, Newsfeed newsfeed);

    Optional<Like> findByUserAndComment(User user, Comment comment);
}
