package com.weshare.controller;

import com.weshare.common.AddGroup;
import com.weshare.common.Result;
import com.weshare.entity.Comment;
import com.weshare.entity.Post;
import com.weshare.service.CommentService;
import com.weshare.service.PostService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	PostService postService;

	/**
	 * 获取指定推文的评论列表
	 */
	@GetMapping("/get/{postId}")
	public Result getCommentList(@PathVariable("postId") Long postId) {
		List<Comment> comments = commentService.getComments(postId);
		if (comments == null) {
			comments = new ArrayList<>();
		}
		return Result.ok().data("commentList", comments);
	}

	/**
	 * 添加评论
	 */
	@PostMapping("/save")
	public Result addComment(@Validated(AddGroup.class) @RequestBody Comment comment) {
		if (!StringUtils.hasText(comment.getContent())) {
			return Result.error().message("评论不可以为空");
		}
		comment.setCreateTime(new Date());
		commentService.saveComments(comment);
		Post post = postService.findByPostId(comment.getPostId());
		postService.increaseCommentCount(post, 1);
		return Result.ok().message("成功添加评论");
	}

	/**
	 * 删除指定id评论
	 */
	@DeleteMapping("/delete/{id}")
	public Result removeComment(@PathVariable("id") Long id, @RequestParam Long userId) {
		Comment comment = commentService.getCommentById(id);
		if (!Objects.equals(comment.getUserId(), userId)) {
			// 推文所属者删除评论
			if (Objects.equals(comment.getBelongUserId(), userId)) {
				commentService.removeComment(comment);
				Post post = postService.findByPostId(comment.getPostId());
				postService.increaseCommentCount(post, -1);
				return Result.ok().message("推文所属者删除评论成功");
			}
			return Result.error().message("当前用户没有权限删除其他用户评论");
		}
		// 自己删除自己的评论
		commentService.removeComment(comment);
		Post post = postService.findByPostId(comment.getPostId());
		postService.increaseCommentCount(post, -1);
		return Result.ok().message("用户删除自己评论成功");
	}
}
