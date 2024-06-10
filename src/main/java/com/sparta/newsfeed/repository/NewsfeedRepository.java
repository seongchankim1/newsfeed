package com.sparta.newsfeed.repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	@Override
	Optional <Newsfeed> findById(Long aLong);

	Newsfeed findByUsername(String username);

	List<Newsfeed> findAllByWriteDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}

