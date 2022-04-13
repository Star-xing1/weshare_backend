package com.weshare.service;

import com.weshare.entity.Verify;
import com.weshare.entity.User;
import com.weshare.entity.UserInfo;
import com.weshare.repo.VerifyTokenRepo;
import com.weshare.repo.UserInfoRepo;
import com.weshare.repo.UserRepo;
import com.weshare.utils.Verification_code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private VerifyTokenRepo confirmationTokenRepository;

    @Autowired
    private EmailSendService emailSenderService;

    @Autowired
    private UserInfoRepo userProfileRepository;

    // 使用spring-security进行密码加密
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public String send(User user) {
        UserInfo existingUser = userProfileRepository.findUserProfileByUsername(user.getUsername());
        if(existingUser !=null)
            return "The username already exists!";
        existingUser = userProfileRepository.findUserProfileByEmail(user.getEmail());
        //如果邮箱已存在,不可注册
        if(existingUser != null) {
           return "The email already exists!";
        }
        else {
            long used_id=1;
            User used=userRepository.findByEmail(user.getEmail());
            if(used!=null)
            {used_id=used.getUserId();}
            confirmationTokenRepository.deleteVerifyByUserId(used_id);
            userRepository.deleteUserByEmail(user.getEmail());
            //保存当前用户,注意设置未激活
            user.setActivated(0);
            String pwd=encoder.encode(user.getPassword());
            user.setPassword(pwd);
            userRepository.save(user);

            //生成随机验证
            String token= new Verification_code().creatCAPTCHA();

            //保存setConfirmationToken
            Verify confirmationToken = new Verify(user.getUserId(), token);
            confirmationTokenRepository.save(confirmationToken);

            //发送邮箱校验
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("【注册】请点开邮件输入邮件中的验证码完成注册!");
            mailMessage.setFrom("hnuweshare@163.com");
            mailMessage.setText("您的注册验证码为：\n\n"+token);

            //发送
            emailSenderService.sendEmail(mailMessage);
            return "The mail has been sent!";
        }
    }

    public String registerUser(User user,String token) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser != null) {
            if(existingUser.getActivated()==1)
                return "The user already exists!";
            else {
                Verify confirmToken = confirmationTokenRepository.findByUserId(existingUser.getUserId());
                System.out.println(confirmToken);
                System.out.println(token);
                //验证成功
                if(confirmToken.getConfirmationToken().equals(token)){
                    existingUser.setActivated(1);		//激活
                    existingUser.setUsername(user.getUsername());   //重新设置用户名
                    userRepository.save(existingUser);

                    //每注册一个user就向资料表中插入一个用户资料
                    UserInfo userProfile=new UserInfo();
                    userProfile.setEmail(user.getEmail());
                    userProfile.setUsername(user.getUsername());
                    userProfileRepository.save(userProfile);
                    return "Sign up is successful!";
                }
                else
                    return "Wrong verification code!Please try again";
            }
        }
        else
            return "Please Send verification code first!";
    }
}
