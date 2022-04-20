package com.weshare.lab4_springboot;

import com.weshare.entity.LikesEnroll;
import com.weshare.entity.Post;
import com.weshare.service.LikesEnrollService;
import com.weshare.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
	@Autowired
	LikesEnrollService likesEnrollService;
	@Autowired
	PostService postService;

	@Test
	public void test01() {
		LikesEnroll likesEnroll = new LikesEnroll();
		likesEnroll.setUserId(36L);
		likesEnroll.setPostId(19L);
		LikesEnroll enroll = likesEnrollService.findLike(likesEnroll.getUserId(), likesEnroll.getPostId());
		System.out.println(enroll);
	}

	@Test
	public void test02() {
		likesEnrollService.giveLike(1111L, 1111L);
	}

	@Test
	public void test03() {
		Post post = postService.findByPostId(19L);
		System.out.println(post);
	}

}
