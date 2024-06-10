package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.CommentResponse;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.service.LikeService;
import com.sparta.newsfeed.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private JwtUtil jwtUtil;

    @PutMapping("/newsfeeds/{id}/like")
    public NewsfeedResponseDto createLike(HttpServletRequest request, @PathVariable Long id) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String username = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token)).getSubject();
        return likeService.toggleLike(username,id);
    }

    @PutMapping("/newsfeed/{newsfeedId}/comments/{id}/like")
    public CommentResponse createLike(HttpServletRequest request, @PathVariable Long newsfeedId, @PathVariable Long id) {
        String token = request.getHeader(JwtUtil.AUTHORIZATION_HEADER);
        String username = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token)).getSubject();
        return likeService.commentLiked(username, newsfeedId, id);
    }
}
