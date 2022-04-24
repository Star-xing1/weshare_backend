package com.weshare.controller;

import com.weshare.common.Result;
import com.weshare.entity.LikesEnroll;
import com.weshare.entity.Post;
import com.weshare.service.LikesEnrollService;
import com.weshare.service.PostService;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
@RequestMapping("/post")
public class PostController {

	@Autowired
	PostService postService;
	@Autowired
	LikesEnrollService likesEnrollService;

	@GetMapping("/getAll")
	public List<Post> getAll() {
		return postService.getAll();
	}

	@PostMapping(value = "/create")
	public String create(@RequestBody Post post) {
		return postService.creat(post);
	}

	@GetMapping("/getByInfoId")
	public List<Post> getByInfoId(@RequestParam("infoId") long infoId) {
		return postService.getByInfoId(infoId);
	}

	@DeleteMapping("/deleteByPostId")
	public Integer deleteByPostId(@RequestParam("postid") long postid) {
		return postService.deleteByPostId(postid);
	}

	/**
	 * 用户点赞功能
	 */
	@PostMapping("/like")
	public Result giveLike(Long userId, Long postId) {
		if(userId == null || postId == null) {
			return Result.error().message("userId或postId为空");
		}
		boolean b = likesEnrollService.giveLike(userId, postId);
		if (!b) {
			return Result.error().message("已经点赞过了");
		}
		Post post = postService.findByPostId(postId);
		if (post == null) {
			return Result.error().message("当前推文不存在");
		}
		postService.increaseLikes(post, 1);
		return Result.ok().message("点赞成功");
	}

	/**
	 * 点赞列表
	 */
	@GetMapping("/allLikes")
	public Result getLikes(Long postId) {
		if (postId == null) {
			return Result.error().message("bad request");
		}
		Post post = postService.findByPostId(postId);
		if (post == null) {
			return Result.error().message("当前推文不存在");
		}
		List<LikesEnroll> likeList = likesEnrollService.findAllByPostId(postId);
		return Result.ok().data("likeCount", likeList.size()).data("likeList", likeList);
	}

	/**
	 * 取消点赞
	 */
	@DeleteMapping("/disableLike")
	public Result disableLike(Long userId, Long postId) {
		Post post = postService.findByPostId(postId);
		if (post == null) {
			return Result.error().message("当前推文不存在");
		}
		LikesEnroll like = likesEnrollService.findLike(userId, postId);
		if (like == null) {
			return Result.error().message("该用户没有给该推文点赞");
		}
		postService.increaseLikes(post, -1);
		likesEnrollService.deleteLikes(userId, postId);
		return Result.ok().message("取消点赞成功");
	}
}

