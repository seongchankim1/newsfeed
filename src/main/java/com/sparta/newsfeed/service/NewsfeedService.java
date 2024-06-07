package com.sparta.newsfeed.service;
import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.repository.NewsfeedRepository;
import com.sparta.newsfeed.repository.UserRepository;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Service
public class NewsfeedService {
    private final NewsfeedRepository newsfeedRepository;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public NewsfeedService(NewsfeedRepository newsfeedRepository, JwtUtil jwtUtil, UserRepository userRepository) {
        this.newsfeedRepository = newsfeedRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    public NewsfeedResponseDto createNewsfeed(NewsfeedRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
        String token = jwtUtil.refreshToken(response, request);
        token = jwtUtil.substringToken(token);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Newsfeed newsfeed = new Newsfeed(requestDto, user);
        newsfeed.setUser(user);
        Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
        return new NewsfeedResponseDto(savedNewsfeed);
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

    @Transactional
    public NewsfeedResponseDto updateNewsfeed(Long id, NewsfeedRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
        String token = jwtUtil.refreshToken(response, request);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElseThrow(()->new IllegalArgumentException("아이디를 찾을 수 없습니다."));
        if (!newsfeed.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 글만 삭제할 수 있습니다.");
        }
        User user = userRepository.findById(newsfeed.getUser().getId()).orElseThrow(()-> new IllegalArgumentException("아이디를 찾을 수 없습니다."));
        newsfeed.updateUpdateDate();
        newsfeed.update(requestDto, user);
        return new NewsfeedResponseDto(newsfeed);
    }

    public String deleteNewsfeed(Long id, HttpServletResponse response, HttpServletRequest request) {
        String token = jwtUtil.refreshToken(response, request);
        String username = jwtUtil.getUserInfoFromToken(token).getSubject();
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("삭제할 뉴스피드가 없습니다."));
        if (!newsfeed.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 뉴스피드만 삭제할 수 있습니다.");
        }
        newsfeedRepository.delete(newsfeed);
        return id + "번 뉴스피드가 삭제되었습니다.";
    }
}