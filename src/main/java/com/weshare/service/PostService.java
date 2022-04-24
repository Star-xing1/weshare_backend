package com.weshare.service;

import com.weshare.entity.Post;
import com.weshare.entity.UserInfo;
import com.weshare.repo.PostRepo;
import com.weshare.repo.UserInfoRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PostService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private UserInfoRepo userProfileRepository;

	public String create(Post post) {
		post.setLikes(0L);
		post.setCommentCount(0L);
		postRepo.save(post);
		UserInfo userinfo = userProfileRepository.findUserProfileByinfoId(post.getInfoId());
		userinfo.setPostCount(userinfo.getPostCount() + 1);
		userProfileRepository.save(userinfo);
		//保证缓存一致性
		String key = "info_id:" + userinfo.getInfoId();
		boolean haskey = redisTemplate.hasKey(key);
		if (haskey) {
			redisTemplate.delete(key);
			System.out.println("删除缓存中的key-----------> " + key);
		}
		return "Post successfully!";
	}

	public List<Post> getAll() {
		return postRepo.findAll(Sort.by(Direction.DESC, "createdAt"));
	}

	public List<Post> getByInfoId(long infoId) {
		return postRepo.findAllByInfoId(infoId, Sort.by(Direction.DESC, "createdAt"));
	}

	public List<Post> getByPostId(long postId) {
		return postRepo.findAllByPostid(postId);
	}

	public Integer deleteByPostId(long postid) {
		return postRepo.deletePostByPostid(postid);
	}

	public Post findByPostId(Long postId) {
		return postRepo.findByPostid(postId);
	}

	/**
	 * 增加点赞数
	 * param: post必须是JPA查询出来的对象
	 */
	public void increaseLikes(Post post, Integer i) {
		if (post == null) {
			return;
		}
		post.setLikes(post.getLikes() + i);
		postRepo.save(post);
	}

	/**
	 * 增加评论数
	 * param: post必须是JPA查询出来的对象
	 */
	public void increaseCommentCount(Post post, Integer i) {
		if (post == null) {
			return;
		}
		post.setCommentCount(post.getCommentCount() + i);
		postRepo.save(post);
	}
}
