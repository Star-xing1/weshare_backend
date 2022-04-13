package com.weshare.service;

import com.weshare.entity.UserInfo;
import com.weshare.repo.UserInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserInfoService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserInfoRepo userProfileRepository;

    public UserInfo findByEmail(String email)
    {
        ValueOperations<String,UserInfo> operations = redisTemplate.opsForValue();
        String key = "info_email:"+email;
        UserInfo res;
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            res = operations.get(key);
        }
        else {
            res=userProfileRepository.findUserProfileByEmail(email);
            operations.set(key,res,5, TimeUnit.HOURS);
        }
        return res;
    }

    public UserInfo findByinfoId(long infoId)
    {
        ValueOperations<String,UserInfo> operations = redisTemplate.opsForValue();
        String key = "info_id:"+infoId;
        UserInfo res;
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            res = operations.get(key);
        }
        else {
            res=userProfileRepository.findUserProfileByinfoId(infoId);
            operations.set(key,res,5, TimeUnit.HOURS);
        }
        return res;
    }

    public UserInfo findByUsername(String username)
    {
        ValueOperations<String,UserInfo> operations = redisTemplate.opsForValue();
        String key = "info_username:"+username;
        UserInfo res;
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            res = operations.get(key);
        }
        else {
            res=userProfileRepository.findUserProfileByUsername(username);
            operations.set(key,res,5, TimeUnit.HOURS);
        }
        return res;
    }

    public List<UserInfo> findAllByUsernameLike(String username)
    {
        List<UserInfo> res;
        res=userProfileRepository.findAllByUsernameContains(username);
        return res;
    }

    public Integer deleteByEmail(String email)
    {
        Integer res;
        String key = "info_email:"+email;
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            redisTemplate.delete(key);
            System.out.println("删除缓存中的key-----------> " + key);
        }
        res=userProfileRepository.deleteUserProfileByEmail(email);
        return res;
    }
    public void save(UserInfo info)
    {
        userProfileRepository.save(info);
    }
    public List<UserInfo> findAll()
    {
        return userProfileRepository.findAll();
    }
}
