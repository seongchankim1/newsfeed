package com.sparta.newsfeed.service;

import com.sparta.newsfeed.dto.ProfileRequestDto;
import com.sparta.newsfeed.dto.ProfileResponseDto;
import com.sparta.newsfeed.entity.Profile;
import com.sparta.newsfeed.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor

public class ProfileService {

    private final ProfileRepository profileRepository;
// 프로필을 조회하는 코드
    public List<ProfileResponseDto> getProfile() {
        return profileRepository.findAll().stream().map(ProfileResponseDto::new).toList();
    }
//  프로필을 수정 update하려는코드
    public Long updateProfiles(Long id, ProfileRequestDto requestDto) {
        Profile profile = findProfileById(id);
        if (id != null && requestDto.getPassword().equals(this.profileRepository.findById(id).get().getPassword())) {
            profile.update(requestDto);
        }
        return id;
    }

    public Profile findProfileById(Long id) {
        return profileRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 ID를 찾을수 없습니다."));
    }
}
