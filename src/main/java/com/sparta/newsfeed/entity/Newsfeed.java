package com.sparta.newsfeed.entity;

import com.sparta.newsfeed.dto.NewsfeedRequestDto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class Newsfeed{
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
		feedUpdated();
	}

	@CreatedDate
	@Column(updatable = false, nullable = false)
	private LocalDateTime writeDate = LocalDateTime.now();

	@LastModifiedDate
	@Column
	private LocalDateTime updateDate;

	@CreatedDate
	@Column
	private LocalDateTime likeCreated;

	@LastModifiedDate
	@Column
	private LocalDateTime  likeUpdated;


	public void feedUpdated(){
		this.updateDate = LocalDateTime.now();
	}

	public void likeCreated(){
		this.likeCreated = LocalDateTime.now();
	}

	public void likeUpdated() {
		this.likeUpdated = LocalDateTime.now();
	}
}