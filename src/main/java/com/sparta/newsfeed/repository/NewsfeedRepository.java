package com.sparta.newsfeed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.newsfeed.entity.Newsfeed;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

}
