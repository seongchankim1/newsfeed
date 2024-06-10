package com.sparta.newsfeed.controller;

import com.sparta.newsfeed.dto.CommentCreateRequest;
import com.sparta.newsfeed.dto.CommentResponse;
import com.sparta.newsfeed.dto.CommentUpdateRequest;
import com.sparta.newsfeed.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{newsfeedId}/comments")
@RequiredArgsConstructor

public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentResponse createComment(@PathVariable(value = "newsfeedId") Long newsfeedId,
                                         @RequestBody CommentCreateRequest requestDto,
                                         HttpServletResponse response,
                                         HttpServletRequest request) {
        return commentService.createComment(newsfeedId, requestDto, response, request);
    }

    @GetMapping("/{id}")
    public CommentResponse findComments(@PathVariable(value = "id") Long id) {
        return commentService.findComment(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @PathVariable(value = "newsfeedId") Long newsfeedId,
            @RequestBody CommentUpdateRequest requestDto,
            HttpServletResponse response,
            HttpServletRequest request) {
        return ResponseEntity.ok().body(commentService.updateComment(id,newsfeedId, requestDto, response, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long id,
            @PathVariable(value = "newsfeedId") Long newsfeedId,
            HttpServletResponse response,
            HttpServletRequest request) {
        commentService.deleteComment(id,newsfeedId, response, request);
        return ResponseEntity.ok().body("성공적으로 댓글 삭제");
    }
}

