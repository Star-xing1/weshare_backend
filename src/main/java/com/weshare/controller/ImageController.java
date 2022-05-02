package com.weshare.controller;

import com.weshare.entity.UserInfo;
import com.weshare.repo.UserInfoRepo;
import com.weshare.repo.UserRepo;
import com.weshare.service.UploadService;
import com.weshare.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    UserInfoRepo userProfileRepository;

    @Autowired
    UploadService uploadService;

    @Autowired
    UserInfoService userInfoService;
    @Autowired
    UserRepo userRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${img.imgURL}")
    private String imgURL;

    //上传图片
    //参数包括用户邮箱和文件
    @PostMapping(value="/userinfo_update")
    public String update(@RequestParam("file") MultipartFile file,@RequestParam("email") String email) {
        UserInfo userProfile = userProfileRepository.findUserProfileByEmail(email);
        if(file==null) return "empty file";
        //如果用户已经存在,更新
        if(userProfile!=null){
            String indexPath=Long.toString(userProfile.getInfoId());
            //处理文件名
            int lastIndexOf = file.getOriginalFilename().lastIndexOf(".");
            //获取文件的后缀名
            String suffix = file.getOriginalFilename().substring(lastIndexOf);
            String fileName=indexPath + "user" + System.currentTimeMillis() + suffix;
            //上传
            uploadService.upload(file,fileName);
            //保存数据库映射
            userProfile.setPath(imgURL+fileName);
            //JPA
            //缓存一致性
            String key = "info_email:"+userProfile.getEmail();
            boolean haskey = redisTemplate.hasKey(key);
            if (haskey) {
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key-----------> " + key);
            }
            key = "info_username:"+userProfile.getUsername();
            haskey = redisTemplate.hasKey(key);
            if (haskey) {
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key-----------> " + key);
            }
            key = "info_id:"+userProfile.getInfoId();
            haskey = redisTemplate.hasKey(key);
            if (haskey) {
                redisTemplate.delete(key);
                System.out.println("删除缓存中的key-----------> " + key);
            }
            userProfileRepository.save(userProfile);
            return "sucess";
        }
        else
            return "Email error";
    }
    @PostMapping(value="/postimg_upload")
    public String postimg_upload(@RequestParam("file") MultipartFile file,@RequestParam("email") String email) {
        UserInfo userProfile = userProfileRepository.findUserProfileByEmail(email);
        if(file==null) return "empty file";
            String indexPath=Long.toString(userProfile.getInfoId());
            //处理文件名
            int lastIndexOf = file.getOriginalFilename().lastIndexOf(".");
            //获取文件的后缀名
            String suffix = file.getOriginalFilename().substring(lastIndexOf);
            String fileName=indexPath + "post" + userProfile.getPostCount() + suffix;
            //上传
            uploadService.upload(file,fileName);
            //保存数据库映射
            return imgURL+fileName;
    }
}
