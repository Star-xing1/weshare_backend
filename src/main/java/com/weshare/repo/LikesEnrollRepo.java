package com.weshare.repo;

import com.weshare.entity.LikesEnroll;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface LikesEnrollRepo extends JpaRepository<LikesEnroll, Long> {
	LikesEnroll findByUserIdAndPostId(Long userId, Long postId);

	List<LikesEnroll> findAllByPostId(Long postId);

	void deleteByUserIdAndPostId(Long userId, Long postId);
}
