package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.NewsfeedRequestDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Newsfeed extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String title;

	@Column
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)

	private User user;
	@Column
	private String username;
	@Column
	private int likes;

	public Newsfeed(NewsfeedRequestDto newsfeedRequestDto, User user) {
		this.title = newsfeedRequestDto.getTitle();
		this.content = newsfeedRequestDto.getContent();
		this.username = user.getUsername();
		this.likes = newsfeedRequestDto.getLike();
	}

	public void update(NewsfeedRequestDto requestDto, User user) {
		this.title = requestDto.getTitle();
		this.content = requestDto.getContent();
		this.username = user.getUsername();
	}

	@OneToMany(mappedBy = "newsfeed", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Comment> comment;
}