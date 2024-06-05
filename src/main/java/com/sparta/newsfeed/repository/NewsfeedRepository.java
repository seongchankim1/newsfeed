package com.sparta.newsfeed.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sun.xml.internal.stream.Entity;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	List<NewsfeedResponseDto> findAllByOrderByCreatedAtDesc();

	List<Newsfeed> findAllByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}
