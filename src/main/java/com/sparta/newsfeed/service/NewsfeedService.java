package com.sparta.newsfeed.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.repository.NewsfeedRepository;

@Service
public class NewsfeedService {

	private final NewsfeedRepository newsfeedRepository;

	public NewsfeedService(NewsfeedRepository newsfeedRepository) {
		this.newsfeedRepository = newsfeedRepository;
	}

	public NewsfeedResponseDto createNewsfeed(NewsfeedRequestDto requestDto) {
		return null;
	}

	public NewsfeedResponseDto getNewsfeed(Long id) {
		return null;
	}

	public List<NewsfeedResponseDto> getAllNewsfeeds() {
		return null;
	}

	public NewsfeedResponseDto updateNewsfeed(Long id, NewsfeedRequestDto requestDto) {
		return null;
	}

	public String deleteNewsfeed(Long id) {
		return null;
	}
}
