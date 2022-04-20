package com.weshare.service;

import com.weshare.entity.LikesEnroll;
import com.weshare.repo.LikesEnrollRepo;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikesEnrollService {
	@Autowired
	LikesEnrollRepo likesEnrollRepo;

	public LikesEnroll findLike(Long userId, Long postId) {
		if (userId == null || postId == null) {
			return null;
		}
		return likesEnrollRepo.findByUserIdAndPostId(userId, postId);
	}

	/**
	 * 用户给推文点赞：注意此方法只影响like_enroll表，其余表的更新在Controller中进行
	 * return true：当点赞成功时返回。当该用户已经给post点过赞，则返回false
	 */
	public boolean giveLike(Long userId, Long postId) {
		LikesEnroll dataEnroll = this.findLike(userId, postId);
		if (dataEnroll == null) {
			LikesEnroll likesEnroll = new LikesEnroll(userId, postId);
			// 这里将id设置为null，让JPA自动设置id字段
			likesEnroll.setId(null);
			likesEnroll.setTime(new Date());
			likesEnrollRepo.save(likesEnroll);
			return true;
		}
		return false;
	}

	public List<LikesEnroll> findAllByPostId(Long postId) {
		return likesEnrollRepo.findAllByPostId(postId);
	}

	public void deleteLikes(Long userId, Long postId) {
		likesEnrollRepo.deleteByUserIdAndPostId(userId, postId);
	}
}
