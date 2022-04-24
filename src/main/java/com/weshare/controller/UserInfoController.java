package com.weshare.controller;

import com.weshare.entity.User;
import com.weshare.entity.UserInfo;
import com.weshare.repo.UserRepo;
import com.weshare.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class UserInfoController {
    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserRepo userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/findAll")
    public List<UserInfo> findAll(){
        return userInfoService.findAll();
    }

    @GetMapping("/findByEmail")
    public UserInfo findProfileByEmail(@RequestParam("email") String email){
        return userInfoService.findByEmail(email);
    }
    @GetMapping("/findByinfoId")
    public UserInfo findProfileByprofileId(@RequestParam("infoId") long infoId){
        return userInfoService.findByinfoId(infoId);
    }


    @GetMapping("/findByUsernameLike")
    public List<UserInfo> findByUsernameLike(@RequestParam("username") String usernmae){
        return userInfoService.findAllByUsernameLike(usernmae);
    }

    @PostMapping(value="/save")
    public String save(@RequestBody UserInfo userProfile) {
        User user=userRepository.findByEmail(userProfile.getEmail());

        if(user!=null){
            user.setUsername(userProfile.getUsername());//更新用户名
            //userRepository.deleteUserByEmail(user.getEmail());
            userRepository.save(user);                  //更新user

            //userProfileRepository.deleteUserProfileByEmail(userProfile.getEmail());
            userInfoService.save(userProfile);    //更新userProfile
            return "sucess";
        }
        else
            return "User Don't Exit, Please Register First!";
    }

    @PostMapping(value="/update")
    public String update(@RequestBody UserInfo userProfile) {
        User user=userRepository.findByEmail(userProfile.getEmail());
        UserInfo userinfo=userInfoService.findByEmail(userProfile.getEmail());
        UserInfo userinfo_exist=userInfoService.findByUsername(userProfile.getUsername());
        if(user!=null){
            if(userinfo_exist==null||userinfo.getInfoId()==userinfo_exist.getInfoId()) {//要更新的用户名不存在
                if (userProfile.getUsername() != null)
                    user.setUsername(userProfile.getUsername());//更新用户名
                //userRepository.deleteUserByEmail(user.getEmail());
                userRepository.save(user);                  //更新user
                if (userProfile.getUsername() != null)
                    userinfo.setUsername(userProfile.getUsername());
                if (userProfile.getName() != null)
                    userinfo.setName(userProfile.getName());
                if (userProfile.getAge() != null)
                    userinfo.setAge(userProfile.getAge());
                if (userProfile.getJob() != null)
                    userinfo.setJob(userProfile.getJob());
                if (userProfile.getPhone() != null)
                    userinfo.setPhone(userProfile.getPhone());
                userInfoService.save(userinfo);    //更新userProfile
                //缓存一致性
                String key = "info_email:"+userinfo.getEmail();
                boolean haskey = redisTemplate.hasKey(key);
                if (haskey) {
                    redisTemplate.delete(key);
                    System.out.println("删除缓存中的key-----------> " + key);
                }
                key = "info_username:"+userinfo.getUsername();
                haskey = redisTemplate.hasKey(key);
                if (haskey) {
                    redisTemplate.delete(key);
                    System.out.println("删除缓存中的key-----------> " + key);
                }
                key = "info_id:"+userinfo.getInfoId();
                haskey = redisTemplate.hasKey(key);
                if (haskey) {
                    redisTemplate.delete(key);
                    System.out.println("删除缓存中的key-----------> " + key);
                }
                return "succeed!";
            }
            return "The username has been used!";
        }
        else
            return "User Don't Exit, Please Register First!";
    }

    @GetMapping("/delete")
    public Integer deleteUserProfileByEmail(@RequestParam("email") String email){
        //缓存一致性
        String key = "info_email:"+email;
        boolean haskey = redisTemplate.hasKey(key);
        if (haskey) {
            redisTemplate.delete(key);
            System.out.println("删除缓存中的key-----------> " + key);
        }
        return  userInfoService.deleteByEmail(email);
    }

}
