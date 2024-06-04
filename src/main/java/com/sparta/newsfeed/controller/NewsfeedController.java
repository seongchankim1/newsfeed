package com.sparta.newsfeed.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.service.NewsfeedService;

@RestController
@RequestMapping("/api/newsfeeds")
public class NewsfeedController {

	private final NewsfeedService newsfeedService;

	public NewsfeedController(NewsfeedService newsfeedService) {
		this.newsfeedService = newsfeedService;
	}

	//뉴스피드 생성 (C)
	@PostMapping
	public NewsfeedResponseDto createNewsfeed(@RequestBody NewsfeedRequestDto requestDto) {
		return newsfeedService.createNewsfeed(requestDto);
	}

	// 뉴스피드 단건 조회 (R)
	@GetMapping("/{id}")
	public NewsfeedResponseDto selectNewsfeed(@PathVariable Long id) {
		return newsfeedService.getNewsfeed(id);
	}

	// 뉴스피드 전체 조회 (R)
	@GetMapping("/all")
	public List<NewsfeedResponseDto> getAllNewsfeeds() {
		return newsfeedService.getAllNewsfeeds();
	}

	// 뉴스피드 수정 (U)
	@PutMapping("/{id}")
	public NewsfeedResponseDto updateNewsfeed(@PathVariable Long id, @RequestBody NewsfeedRequestDto requestDto) {
		return newsfeedService.updateNewsfeed(id,requestDto);
	}

	// 뉴스피드 삭제 (D)
	@DeleteMapping("/{id}")
	public String deleteNewsfeed(@PathVariable Long id) {
		return newsfeedService.deleteNewsfeed(id);
	}
}
