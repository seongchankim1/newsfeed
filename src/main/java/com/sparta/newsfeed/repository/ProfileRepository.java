package com.sparta.newsfeed.repository;

import com.sparta.newsfeed.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
