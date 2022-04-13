package com.weshare.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.weshare.entity.User;

import javax.transaction.Transactional;

@Transactional
public  interface  UserRepo extends JpaRepository<User, Integer>{
    User findByEmail(String email);
    User findByUsername(String username);
    Integer deleteUserByEmail(String email);
}
