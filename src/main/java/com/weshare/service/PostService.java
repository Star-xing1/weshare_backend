package com.weshare.service;
import com.weshare.entity.Post;
import com.weshare.entity.UserInfo;
import com.weshare.repo.PostRepo;
import com.weshare.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepo PostRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserInfoRepo userProfileRepository;

    public String creat(Post post) {
        PostRepository.save(post);
        UserInfo userinfo=userProfileRepository.findUserProfileByinfoId(post.getInfoId());
        userinfo.setPostCount(userinfo.getPostCount()+1);
        userProfileRepository.save(userinfo);
        //保证缓存一致性
        String key = "info_id:"+userinfo.getInfoId();
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            redisTemplate.delete(key);
            System.out.println("删除缓存中的key-----------> " + key);
        }
        return "Post successfully!";
    }
    public List<Post> getAll() {
        return PostRepository.findAll(Sort.by(Direction.DESC, "createdAt"));
    }
    public List<Post> getByInfoId(long infoId) { return PostRepository.findAllByInfoId(infoId, Sort.by(Direction.DESC, "createdAt")); }
    public Integer deleteByPostId(long postid) { return PostRepository.deletePostByPostid(postid); }
}
