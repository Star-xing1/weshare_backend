package com.weshare.repo;

import com.weshare.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserInfoRepo extends JpaRepository<UserInfo, Integer> {
    UserInfo findUserProfileByEmail(String email);
    UserInfo findUserProfileByinfoId(long infoId);
    UserInfo findUserProfileByUsername(String username);
    List<UserInfo> findAllByUsernameContains(String username);

    Integer deleteUserProfileByEmail(String email);
}
