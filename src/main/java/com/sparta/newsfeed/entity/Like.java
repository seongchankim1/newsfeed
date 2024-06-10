package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "likes")
public class Like{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "newsfeed_id", nullable = false)
    private Newsfeed newsfeed;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;
//    @Column
//    private Long ContentId;
//
//    @Column
//    private String ContentType;

}
