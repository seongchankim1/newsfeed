package com.sparta.newsfeed.service;
import com.sparta.newsfeed.dto.NewsfeedRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.dto.PagingRequestDto;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.jwt.JwtUtil;
import com.sparta.newsfeed.repository.NewsfeedRepository;
import com.sparta.newsfeed.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    // 뉴스피드 생성
    @Transactional
    public NewsfeedResponseDto createNewsfeed(NewsfeedRequestDto requestDto,
                                              HttpServletResponse response,
                                              HttpServletRequest request) {
        // 토큰 해석 및 갱신
        String token = jwtUtil.resolveToken(request);
        String newAccessToken = jwtUtil.refreshToken(token, response);
        String username = jwtUtil.getUserInfoFromToken(newAccessToken).getSubject();
        // 유저 확인
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        // 뉴스피드 생성
        Newsfeed newsfeed = new Newsfeed(requestDto, user);
        newsfeed.setUser(user);
        Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
        return new NewsfeedResponseDto(savedNewsfeed);
    }

    // 뉴스피드 조회 by ID
    public NewsfeedResponseDto getNewsfeed(Long id) {
        Newsfeed newsfeed = newsfeedRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("조회된 뉴스피드가 없습니다. id=" + id));
        return new NewsfeedResponseDto(newsfeed);
    }

    // 모든 뉴스피드 조회
    public List<NewsfeedResponseDto> getAllNewsfeeds() {
        List<Newsfeed> newsfeedList = newsfeedRepository.findAll();
        return newsfeedList.stream()
            .map(NewsfeedResponseDto::new)
            .collect(Collectors.toList());
    }

    // 뉴스피드 업데이트
    @Transactional
    public NewsfeedResponseDto updateNewsfeed(Long id, NewsfeedRequestDto requestDto, HttpServletResponse response, HttpServletRequest request) {
        // 토큰 해석 및 갱신
        String token = jwtUtil.resolveToken(request);
        String newAccessToken = jwtUtil.refreshToken(token, response);
        String newBearerAccessToken = jwtUtil.substringToken(newAccessToken);
        String username = jwtUtil.getUserInfoFromToken(newBearerAccessToken).getSubject();
        // 뉴스피드 조회
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 뉴스피드를 찾을 수 없습니다."));
        // 작성자 확인
        if (!newsfeed.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 글만 수정할 수 있습니다.");
        }
        // 유저 조회
        User user = userRepository.findById(newsfeed.getUser().getId()).orElseThrow(()-> new IllegalArgumentException("아이디를 찾을 수 없습니다."));
        newsfeed.updateUpdateDate();
        newsfeed.update(requestDto, user);
        return new NewsfeedResponseDto(newsfeed);
    }

    // 뉴스피드 삭제
    @Transactional
    public String deleteNewsfeed(Long id, HttpServletResponse response, HttpServletRequest request) {
        // 토큰 해석 및 갱신
        String token = jwtUtil.resolveToken(request);
        String newAccessToken = jwtUtil.refreshToken(token, response);
        String newBearerAccessToken = jwtUtil.substringToken(newAccessToken);
        String username = jwtUtil.getUserInfoFromToken(newBearerAccessToken).getSubject();
        // 뉴스피드 조회
        Newsfeed newsfeed = newsfeedRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("삭제할 뉴스피드가 없습니다."));
        // 작성자 확인
        if (!newsfeed.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("자신의 뉴스피드만 삭제할 수 있습니다.");
        }
        // 뉴스피드 삭제
        newsfeedRepository.delete(newsfeed);
        return id + "번 뉴스피드가 삭제되었습니다.";
    }

    // 페이징 처리된 뉴스피드 조회
    public List<NewsfeedResponseDto> getNewsfeeds(Long page, PagingRequestDto requestDto) {
        Stream<Newsfeed> newsfeedStream;
        // 기간, 정렬 방식 설정
        LocalDateTime startDate = requestDto.getStartDate();
        LocalDateTime endDate = requestDto.getEndDate();
        String sortBy = requestDto.getSortBy();
        // 기간별 검색, 아닐 시 전체 조회
        if (startDate != null && endDate != null) {
            newsfeedStream = newsfeedRepository.findAllByWriteDateBetween(startDate, endDate).stream();
        } else {
            newsfeedStream = newsfeedRepository.findAll().stream();
        }
        // null 값 시 최신 순
        if (sortBy == null) {
            sortBy = "writeDate";
        }
        // likes 시 좋아요순, 아닐 시 최신 순
        if (sortBy.equals("likes")) {
            newsfeedStream = newsfeedStream.sorted(Comparator.comparing(Newsfeed::getLikes).reversed());
        } else {
            newsfeedStream = newsfeedStream.sorted(Comparator.comparing(Newsfeed::getWriteDate).reversed());
        }
        // 페이지 별 10개씩 나누는 작업
        return newsfeedStream
            .skip((page - 1) * 10L)
            .limit(10)
            .map(NewsfeedResponseDto::new)
            .toList();
    }
}