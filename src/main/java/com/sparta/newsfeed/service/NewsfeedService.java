package com.sparta.newsfeed.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
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

	public List<NewsfeedResponseDto> getNewsfeeds(Long page, LocalDateTime startDate, LocalDateTime endDate, String sortBy) {
		Stream<Newsfeed> newsfeedStream;

		// 기간별 검색
		if (startDate != null && endDate != null) {
			newsfeedStream = newsfeedRepository.findAllByCreatedAtBetween(startDate, endDate).stream();
		} else {
			newsfeedStream = newsfeedRepository.findAll().stream();
		}

		if (sortBy.equals("like")) {
			newsfeedStream = newsfeedStream.sorted(Comparator.comparing(Newsfeed::getLike).reversed());
		} else {
			newsfeedStream = newsfeedStream.sorted(Comparator.comparing(Newsfeed::getCreatedAt).reversed());
		}

		return newsfeedStream
			.skip(page * 10L)
			.limit(10)
			.map(NewsfeedResponseDto::new)
			.toList();
	}
}
