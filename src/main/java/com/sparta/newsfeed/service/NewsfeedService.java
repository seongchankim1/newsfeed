package com.sparta.newsfeed.service;

import java.util.List;
import java.util.stream.Collectors;

import com.sparta.newsfeed.entity.Newsfeed;
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

		Newsfeed newsfeed = new Newsfeed();
		newsfeed.setTitle(requestDto.getTitle());
		newsfeed.setContent(requestDto.getContent());

		Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
		NewsfeedResponseDto responseDto = new NewsfeedResponseDto(savedNewsfeed);
		return responseDto;
	}

	public NewsfeedResponseDto getNewsfeed(Long id) {
		Newsfeed newsfeed = newsfeedRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("조회된 뉴스피드가 없습니다. id=" + id));
		return new NewsfeedResponseDto(newsfeed);
	}

	public List<NewsfeedResponseDto> getAllNewsfeeds() {
		List<Newsfeed> newsfeedList = newsfeedRepository.findAll();

		return newsfeedList.stream()
				.map(NewsfeedResponseDto::new)
				.collect(Collectors.toList());
	}

	public NewsfeedResponseDto updateNewsfeed(Long id, NewsfeedRequestDto requestDto) {
		return null;
	}

	public String deleteNewsfeed(Long id) {
		return null;
	}
}
