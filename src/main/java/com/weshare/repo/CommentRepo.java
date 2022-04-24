package com.weshare.repo;

import com.weshare.entity.Comment;
import java.util.List;
import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CommentRepo extends JpaRepository<Comment, Long> {

	List<Comment> findByUserIdAndPostId(Long userId, Long postId);

	List<Comment> findByPostId(Long postId,Sort sort);
}
