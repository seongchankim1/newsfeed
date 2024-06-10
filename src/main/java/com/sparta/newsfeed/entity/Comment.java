package com.sparta.newsfeed.entity;

import java.sql.Time;
import java.time.LocalDateTime;

import ch.qos.logback.core.net.SMTPAppenderBase;
import com.sparta.newsfeed.dto.CommentCreateRequest;
import com.sparta.newsfeed.dto.CommentUpdateRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@Setter
@Table
@NoArgsConstructor

public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // 사용자 ID
    private String nickname; // 사용자별명

    @Column(nullable = false)
    @NotBlank(message = "공백을 허용하지 않습니다.")
    private String comment;

    @Column(nullable = false)
    private long good_counting;

    @Column
    private int likes;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = true)
    private Newsfeed newsfeed;


    public Comment(CommentCreateRequest commentCreateRequest, Newsfeed newsfeed) {
        this.username = commentCreateRequest.getUsername();
        this.comment = commentCreateRequest.getComment();
        this.newsfeed = newsfeed;
        this.nickname = commentCreateRequest.getNickname();
    }

    public void update(CommentUpdateRequest requestDto, Newsfeed newsfeed) {
        this.comment = requestDto.getComment();
    }

    @CreatedDate
    @Column
    private LocalDateTime likeCreated;

    @LastModifiedDate
    @Column
    private LocalDateTime  likeUpdated;

    public void likeCreated(){
        this.likeCreated = LocalDateTime.now();
    }

    public void likeUpdated() {
        this.likeUpdated = LocalDateTime.now();
    }

//    public Comment(String comment, String username, Newsfeed newsfeed) {
//        this.comment = comment;
//        this.nickname = username;
//        this.newsfeed = newsfeed;

//    }
}























//    public Comment(Newsfeed newsfeed, String comment, String nickname, String username,
//                   long good_counting, long id, long newfeesId) {
//        this.newsfeed = newsfeed;
//        this.nickname = nickname;
//        this.comment = comment;
//        this.username = username;
//        this.good_counting = good_counting;
//        this.id = id;

//        this.newfeesId = newfeesId;
    //    }

