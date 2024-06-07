package com.sparta.newsfeed.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	Newsfeed findByUsername(String username);

	List<Newsfeed> findAllByWriteDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
