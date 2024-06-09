package com.sparta.newsfeed;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.sparta.newsfeed.service.UserService;

@SpringBootTest
public class AuthKeyTest {

	@Autowired
	private UserService userService;

	@Test
	public void test() {
		String username = "hi";
		userService.authKeyBuilder();

	}
}
