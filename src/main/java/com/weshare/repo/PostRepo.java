package com.weshare.repo;

import com.weshare.entity.Post;
import com.weshare.entity.UserInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface PostRepo extends JpaRepository<Post, Integer> {

    Integer deletePostByPostid(long post_id);
    Post findPostByPostid(String post_id);
    List<Post> findAllByInfoId(long infoId, Sort sort);
}