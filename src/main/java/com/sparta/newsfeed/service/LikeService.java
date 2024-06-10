package com.sparta.newsfeed.service;

// import com.sparta.newsfeed.dto.LikeRequestDto;
import com.sparta.newsfeed.dto.NewsfeedResponseDto;
import com.sparta.newsfeed.entity.Like;
import com.sparta.newsfeed.entity.Newsfeed;
import com.sparta.newsfeed.entity.User;
import com.sparta.newsfeed.repository.LikeRepository;
import com.sparta.newsfeed.repository.NewsfeedRepository;
import com.sparta.newsfeed.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService {

    private static NewsfeedRepository newsfeedRepository;
    private static LikeRepository likeRepository;
    private static UserRepository userRepository;

    public LikeService(NewsfeedRepository newsfeedRepository, LikeRepository likeRepository, UserRepository userRepository) {
        this.newsfeedRepository = newsfeedRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public NewsfeedResponseDto toggleLike(String username, Long newsfeedId) {
        Newsfeed newsfeed = newsfeedRepository.findById(newsfeedId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시물 입니다."));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        if(newsfeed.getUsername().equals(username)) {
            throw new IllegalArgumentException("본인이 작성한 게시물 입니다.");
        }

        Optional<Like> existLike = likeRepository.findByUserAndNewsfeed(user, newsfeed);

        if(existLike.isPresent()) {
            likeRepository.delete(existLike.get());
            newsfeed.likeUpdated();
            newsfeed.setLikes(newsfeed.getLikes() - 1);
        } else{
            Like like = new Like();
            like.setUser(user);
            like.setNewsfeed(newsfeed);
            newsfeed.likeCreated();
            newsfeed.likeUpdated();
            likeRepository.save(like);
            newsfeed.setLikes(newsfeed.getLikes() + 1);
        }


        Newsfeed savedNewsfeed = newsfeedRepository.save(newsfeed);
        return new NewsfeedResponseDto(savedNewsfeed);
    }
}
