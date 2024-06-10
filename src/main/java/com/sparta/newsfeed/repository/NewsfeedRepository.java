package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

	@Override
	Optional <Newsfeed> findById(Long aLong);

	Newsfeed findByUsername(String username);

}

