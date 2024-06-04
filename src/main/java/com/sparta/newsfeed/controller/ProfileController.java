package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.ProfileRequestDto;
import com.sparta.newsfeed.dto.ProfileResponseDto;
import com.sparta.newsfeed.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/{id}/profile")

public class ProfileController {

    private final ProfileService profileService;

    @GetMapping //조회기능 구현위치
    public List<ProfileResponseDto> getProfiles() {
        return profileService.getProfile();
    }

    @PutMapping("/{profileid}") //수정기능 구현위치
    public Long updateProfiles(@PathVariable Long id, @RequestBody ProfileRequestDto requestDto) {
        return profileService.updateProfiles(id, requestDto);
    }
}
