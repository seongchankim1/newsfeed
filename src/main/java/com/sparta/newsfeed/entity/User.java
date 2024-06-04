package com.sparta.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	private String email;
	private String introduce;
	private String status;
	private Timestamp created;
	private Timestamp updated;



	public User(String username, String password , String email, String introduce, String status) {

		this.username = username;
		this.password = password;
		this.email = email;
		this.introduce = introduce;
		this.status = status;
		this.created = new Timestamp(System.currentTimeMillis());
		this.updated = new Timestamp(System.currentTimeMillis());
	}
}
