package com.weshare.service;

import com.weshare.entity.Comment;
import com.weshare.entity.Post;
import com.weshare.exception.UnknownPostException;
import com.weshare.repo.CommentRepo;
import com.weshare.repo.PostRepo;
import com.weshare.repo.UserRepo;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CommentService {
	@Autowired
	CommentRepo commentRepo;
	@Autowired
	PostRepo postRepo;
	@Autowired
	UserRepo userRepo;

	/**
	 * 获取评论列表
	 */
	public List<Comment> getComments(Long postId) {
		if (postId == null) {
			return null;
		}
		return commentRepo.findByPostId(postId);
	}

	public Comment getCommentById(Long id) {
		if (id == null) {
			return null;
		}
		return commentRepo.getById(id);
	}

	/**
	 * 添加评论
	 */
	public void saveComments(Comment comment) {
		if (comment == null || !StringUtils.hasText(comment.getContent())
				|| comment.getUserId() == null || comment.getPostId() == null) {
			return;
		}
		// TODO 异步查询，优化速率
		Post post = postRepo.findByPostid(comment.getPostId());
		// 判断对应的推文和用户是否存在
		if (post == null) {
			throw new UnknownPostException();
		}
		comment.setContent(comment.getContent().trim());
		comment.setCreateTime(new Date());
		comment.setId(null);
		commentRepo.save(comment);
		return;
	}

	/**
	 * 用户删除评论：需要提供id、userId、postId
	 */
	public void removeComment(Comment comment) {
		commentRepo.delete(comment);
	}
}
